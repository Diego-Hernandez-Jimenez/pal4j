# install.packages('data.table')

minmax_scale <- function(x, na.rm = TRUE) {
  return((x - min(x)) /(max(x) - min(x)))
}

train_test_split <- function(train_proportion, dt) {
  n_train <- floor(train_proportion * nrow(dt))
  train_ids <- sample(1:nrow(dt), n_train, replace=F)
  splits <- list(train=dt[train_ids], test=dt[-train_ids])
  return(splits)
}

base_path <- 'C:/Users/Diego/javaprojects/raw_datasets'
out_path <- 'C:/Users/Diego/javaprojects/processed_datasets'

##############################################

# anomaly detection: phishing (https://archive.ics.uci.edu/dataset/967/phiusiil+phishing+url+dataset)

cols_to_drop <- c(
  'FILENAME',
  'URL',
  'Domain',
  'TLD',
  'URLSimilarityIndex',
  'Title'
)
dt <- data.table::fread(file.path(base_path, 'PhiUSIIL_Phishing_URL_Dataset.csv'), sep=',', drop=cols_to_drop)

dt[, label := ifelse(label == 1, -1, 1)] # 1 -> -1 = legit, 0 -> 1 = potential phising

dt_normal <- dt[label == -1,]
dt_anomaly <- dt[label == 1,]

normal_ids_test <- sample(1:nrow(dt_normal), floor(0.1 * nrow(dt_normal)), replace=F)
abnormal_ids_test <- sample(1:nrow(dt_anomaly), length(normal_ids_test), replace=F)

# during training the model will only see normal instances
dt_train <- dt_normal[-normal_ids_test,]

# at inference time the model sees both normal and abnormal instances. In this case, anomalous cases are more frequent
dt_test <- data.table::rbindlist(
  list(
    dt_normal[normal_ids_test,],
    dt_anomaly
  )
)

data.table::fwrite(dt_train, file.path(out_path, 'ad_phishing_train.csv'))
data.table::fwrite(dt_test, file.path(out_path, 'ad_phishing_test.csv'))

# binary classification version: we keep the original class distribution
splits <- train_test_split(0.8, dt)

data.table::fwrite(splits$train, file.path(out_path, 'binary_phishing_train.csv'))
data.table::fwrite(splits$test, file.path(out_path, 'binary_phishing_test.csv'))



##############################################

# regression/time series: electricity demand (https://www.kaggle.com/datasets/aramacus/electricity-demand-in-victoria-australia?select=complete_dataset.csv)

cols_to_keep <- c(
  'demand',
  'RRP',
  'min_temperature',
  'max_temperature',
  'solar_exposure',
  'rainfall',
  'school_day',
  'holiday'
)
dt <- data.table::fread(file.path(base_path, 'electricity_demand_dataset.csv'), sep=',', select=cols_to_keep)
dt <- dt[complete.cases(dt), ]

dt[, dummy_school_day := ifelse(school_day == 'Y', 1, 0)]
dt[, dummy_holiday := ifelse(holiday == 'Y', 1, 0)]
dt$school_day <- NULL
dt$holiday <- NULL

dt[, `:=`(
  demand_lag1 = data.table::shift(demand, 1, fill=0),
  demand_lag2 = data.table::shift(demand, 2, fill=0),
  demand_lag3 = data.table::shift(demand, 3, fill=0),
  demand_lag4 = data.table::shift(demand, 4, fill=0)
)]

dt[, `:=`(
  solar_exposure_lag1 = data.table::shift(solar_exposure, 1, fill=0),
  solar_exposure_lag2 = data.table::shift(solar_exposure, 2, fill=0),
  solar_exposure_lag3 = data.table::shift(solar_exposure, 3, fill=0)
)]
dt$solar_exposure <- NULL

dt[, `:=`(
  min_temp_lag1 = data.table::shift(min_temperature, 1, fill=0),
  min_temp_lag2 = data.table::shift(min_temperature, 2, fill=0),
  min_temp_lag3 = data.table::shift(min_temperature, 3, fill=0)
)]
dt$min_temperature <- NULL

dt[, `:=`(
  max_temp_lag1 = data.table::shift(max_temperature, 1, fill=0),
  max_temp_lag2 = data.table::shift(max_temperature, 2, fill=0),
  max_temp_lag3 = data.table::shift(max_temperature, 3, fill=0)
)]
dt$max_temperature <- NULL


dt[, `:=`(
  rainfall_lag1 = data.table::shift(rainfall, 1, fill=0),
  rainfall_lag2 = data.table::shift(rainfall, 2, fill=0),
  rainfall_lag3 = data.table::shift(rainfall, 3, fill=0)
)]
dt$rainfall <- NULL

dt[, `:=`(
  rrp_lag1 = data.table::shift(RRP, 1, fill=0),
  rrp_lag2 = data.table::shift(RRP, 2, fill=0),
  rrp_lag3 = data.table::shift(RRP, 3, fill=0)
)]
dt$RRP <- NULL

n_dt <- dt[, .N]
n_train <- floor(0.8 * n_dt)
dt_train <- dt[1:n_train]
dt_test <- dt[(n_train+1):n_dt]
data.table::fwrite(dt_train, file.path(out_path, 'demand_train.csv'))
data.table::fwrite(dt_test, file.path(out_path, 'demand_test.csv'))

# reference: linear regression model
model <- lm(demand ~ demand_lag1 + demand_lag2 + demand_lag3 + demand_lag4 + dummy_holiday, data=dt_train)
preds <- predict(model, newdata = dt_test)

# MAPE
100 * mean( abs(dt_test$demand - preds) / dt_test$demand )


##############################################

# binary classification: breast cancer (https://archive.ics.uci.edu/dataset/15/breast+cancer+wisconsin+original)

dt <- data.table::fread(file.path(base_path, 'wdbc.data'))

# change label values {0, 1} --> {-1, 1}
dt[, diagnosis := ifelse(V2 == 'M', 1, -1)]
dt$V1 <- NULL
dt$V2 <- NULL

# scale features
features <- colnames(dt)[1:30]
dt[, (features) := lapply(.SD, minmax_scale), .SDcols=features ]

splits <- train_test_split(0.8, dt)

data.table::fwrite(splits$train, file.path(out_path, 'breast_cancer_scaled_train.csv'))
data.table::fwrite(splits$test, file.path(out_path, 'breast_cancer_scaled_test.csv'))


##############################################

# binary classification: occupancy detection (https://archive.ics.uci.edu/dataset/357/occupancy+detection)

for (data_split in c('training', 'test')) {
  dt <- data.table::fread(file.path(base_path, 'occupancy+detection', sprintf('data%s.txt', data_split)), drop=1)
  
  dt[, Occupancy := ifelse(Occupancy == 1, 1, -1)]
  
  data.table::fwrite(dt, file.path(out_path, sprintf('occupancy_%s.csv', data_split)))
}


##############################################

# regression: seoul bike data (https://archive.ics.uci.edu/datasets?search=Seoul%20Bike%20Sharing%20Demand)

dt <- data.table::fread(file.path(base_path, 'SeoulBikeData.csv'), encoding = 'Latin-1')
colnames(dt) <- sapply(tolower(colnames(dt)), function(name) gsub('\\s*\\([^\\)]+\\)', '', name)) # simplify column names removing measurement units
dt[, dummy_holiday := ifelse(holiday == 'Holiday', 1, 0)]
dt[, dummy_functioning_day := ifelse(`functioning day` == 'Yes', 1, 0)]
dt[, dummy_winter := ifelse(seasons == 'Winter', 1, 0)]
dt[, dummy_spring := ifelse(seasons == 'Spring', 1, 0)]
dt[, dummy_summer := ifelse(seasons == 'Summer', 1, 0)]
dt[, dummy_autumn := ifelse(seasons == 'Autumn', 1, 0)]

dt$seasons <- NULL
dt$holiday <- NULL
dt$`functioning day` <- NULL

dt[, `:=`(
  night = data.table::fifelse(hour %in% 0:5 | hour %in% 21:23, 1, 0),
  morning = data.table::fifelse(hour %in% 6:11, 1, 0),
  afternoon = data.table::fifelse(hour %in% 12:16, 1, 0),
  evening = data.table::fifelse(hour %in% 17:20, 1, 0)
)]

dt$hour <- NULL

n_dt <- dt[, .N]
n_train <- floor(0.8 * n_dt)
dt_train <- dt[1:n_train]
dt_test <- dt[(n_train+1):n_dt]

data.table::fwrite(dt_train, file.path(out_path, 'seoul_bike_train.csv'))
data.table::fwrite(dt_test, file.path(out_path, 'seoul_bike_test.csv'))


features <- c(
  'temperature',
  'humidity',
  'wind speed',
  'visibility',
  'solar radiation',
  'rainfall',
  'snowfall',
  'dummy_holiday',
  'dummy_functioning_day',
  'dummy_winter',
  'dummy_spring',
  'dummy_summer',
  'dummy_autumn',
  'night',
  'morning',
  'afternoon',
  'evening'
)
dt[, (features) := lapply(.SD, minmax_scale), .SDcols=features ]
dt_train <- dt[1:n_train]
dt_test <- dt[(n_train+1):n_dt]
data.table::fwrite(dt_train, file.path(out_path, 'seoul_bike_scaled_train.csv'))
data.table::fwrite(dt_test, file.path(out_path, 'seoul_bike_scaled_test.csv'))


##############################################

# anomaly detection https://github.com/GuansongPang/ADRepository-Anomaly-detection-datasets/blob/main/numerical%20data/DevNet%20datasets/creditcardfraud_normalised.tar.xz

dt <- data.table::fread(file.path(base_path, 'creditcardfraud_normalised.csv'))

dt[, `class` := ifelse(`class` == 1, 1, -1)]

data.table::setnames(dt, old='Amount', new='V29')
dt_normal <- dt[`class` == -1,]
dt_anomaly <- dt[`class` == 1,]

normal_ids_test <- sample(1:nrow(dt_normal), nrow(dt_anomaly), replace=F)
dt_train <- dt_normal[-normal_ids_test,]
dt_test <- data.table::rbindlist(
  list(
    dt_normal[normal_ids_test,],
    dt_anomaly
  )
)

data.table::fwrite(dt_train, file.path(out_path, 'fraud_scaled_train.csv'))
data.table::fwrite(dt_test, file.path(out_path, 'fraud_scaled_test.csv'))
