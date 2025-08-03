## pal4j: Passive-Aggressive Learning algorithms for Java

---

This project is inspired by the work of Shalev-Shwartz et al. (2003) and, in particular, Crammer et al. (2006). From the various algorithms presented in Crammer et al. (2006), I’ve chosen to implement only a subset:

* **Binary classifier**, in both linear and non-linear forms. While the authors don’t provide full details for the kernelized (non-linear) version, insights from related models like SVMs can be applied effectively.
* **Regressor**, also available in linear and non-linear variants.
* **Anomaly detector**. The original paper discusses the broader problem of uniclass prediction; I’ve adapted this slightly to suit anomaly detection. Only the linear version has been implemented so far.

This library has no external dependencies, which meant building several support components from scratch to ensure everything functions correctly:

* A dedicated **dataset** class designed for online learning.
* A **trainer** class to simplify the training process.
* Custom **performance metrics** (e.g., accuracy, MAE).
* Core **vector operations** (e.g., norms, dot products).
* ...and a few other utilities.

Some basic tests have been written, though I recognize that more are needed to improve the robustness of the library.

The following diagrams can give a complete picture of the structure of the library:

### Class diagram
![class diagram](src/main/resources/pal4j_class_diagram.png)

### Class relations
![class diagram](src/main/resources/pal4j_basic_class_relations.png)

---

### Getting started

A Maven package is not available yet. To use the library, clone the repo and compile it manually.

#### Sample Usage
```java
var features = new String[] {"x1", "x2"};
var target = "y";
var delimiter = ",";

var trainData= new BufferedDataset(
        "train_data.csv",
        delimiter,
        features,
        target

);
var testData= new BufferedDataset(
        "test_data.csv",
        delimiter,
        features,
        target

);

var C = 0.1;
var model = new PABinaryClassifier("PA_1", C, features.length);

var metric = new Accuracy();
var trainer = new SupervisedTrainer(model, trainData, metric);

trainer.train(null); // use the full dataset
trainer.evaluate(testData);
```


---
### References

Shalev-Shwartz, S., Crammer, K., Dekel, O., & Singer, Y. (2003). Online passive-aggressive algorithms. *Advances in neural information processing systems*, 16.

Crammer, K., Dekel, O., Keshet, J., Shalev-Shwartz, S., & Singer, Y. (2006). Online passive-aggressive algorithms. *Journal of Machine Learning Research*, 7(Mar), 551-585.

---

Proudly developed without the aid of generative AI tools.