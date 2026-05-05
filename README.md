# SeqSriteANNHybridTransformer
Voici ton **README.md final prêt à copier directement dans ton dépôt GitHub** 👇

---

```markdown
# SeqSGriteMinerANN  
**A Hybrid Transformer Framework for Mining Gradual Sequential Patterns**

---

## 📌 Overview

This project implements a **hybrid symbolic–neural framework** for mining and modeling **gradual sequential patterns** from numerical datasets.

The system combines:

- Gradual pattern mining (SGrite-based)
- Sequential pattern mining (PrefixSpan adaptation)
- Transformer-based regression modeling

The objective is to bridge:

✔ interpretable pattern mining  
✔ deep learning sequence modeling  

---

## 🧠 Key Contributions

- Extraction of **gradual sequential patterns**
- Construction of **pseudo-projected databases**
- Integration of **GS, WGS, ACC features**
- Transformer-based **multi-output regression**
- Complete experimental pipeline:
  - Baseline comparison
  - Ablation study
  - Statistical validation

---

## ⚙️ Architecture

```

Numerical Dataset
↓
Java Module (SGrite + PrefixSpanGS)
↓
Gradual Sequential Database (bdsequentiel.txt)
↓
Python Module (TransformerRegressor)
↓
Predictions (GS, WGS, ACC)
↓
Evaluation (Notebook)

```

---

## 📁 Project Structure

```

SeqSGriteMinerANN/
├── java/
│   └── SeqSGriteMiner.java
├── pythonScript/
│   └── ann.py
├── seqtransformergradualrev3.ipynb
├── data/
├── outputs/
├── venv/
├── requirements.txt
└── README.md

````

---

## 🛠️ Installation

### 1. Clone repository

```bash
git clone [https://github.com/yourusername/SeqSGriteMinerANN.git](https://github.com/tafoca/SeqSriteANNHybridTransformer/)
cd SeqSGriteMinerANN
````

---

### 2. Create virtual environment

```bash
python3 -m venv venv
source venv/bin/activate   # Linux / Mac
# or
venv\Scripts\activate      # Windows
```

---

### 3. Install dependencies

```bash
pip install -r requirements.txt
```

---

## ▶️ Usage

### 🔹 Step 1 — Run Java mining module

```bash
cd java
javac SeqSGriteMiner.java
java SeqSGriteMiner
```

This step:

* Loads dataset
* Extracts gradual patterns
* Generates sequential database (`bdsequentiel.txt`)
* Applies PrefixSpan-based mining

---

### 🔹 Step 2 — Run Python neural model

```bash
python pythonScript/ann.py
```

This step:

* Loads mined sequences
* Trains Transformer model
* Predicts GS, WGS, ACC

---

### 🔹 Step 3 — Run experiments (recommended)

```bash
jupyter notebook seqtransformergradualrev3.ipynb
```

Includes:

* Baseline comparison (Point 7)
* Ablation study (Point 8)
* Statistical tests
* Visualization (figures used in the paper)

---

## 📊 Outputs

```
├── baseline_outputs/
│   ├── baseline_comparison_results.csv
│   ├── baseline_bar_chart.png
├── ablation_outputs/
│   ├── ablation_performance_comparison.png
│   ├── training_loss_curves.png
│   ├── convergence_speed_comparison.png
│   ├── runtime_memory_comparison.png
│   ├── ttest_pvalues_comparison.png
```

---

## 📈 Evaluation Metrics

* MAE (Mean Absolute Error)
* RMSE (Root Mean Squared Error)
* Pearson Correlation
* R² Score
* Statistical significance (t-test)

---

## 🔬 Reproducibility

To reproduce results:

1. Prepare dataset (CSV format)
2. Run Java pipeline
3. Execute Python model
4. Run notebook experiments

✔ Full pipeline reproducible
✔ All results in the paper can be regenerated

---

## 📚 Dependencies

Main libraries:

* Python 3.x
* PyTorch
* NumPy
* SciPy
* scikit-learn
* Matplotlib
* Seaborn

---

## 📄 Paper

This repository accompanies the paper:

**"A Hybrid Transformer Framework for Mining Gradual Sequential Patterns"**

Submitted to: *WIREs Data Mining and Knowledge Discovery*

---

## 🔗 Code Availability

All source code, scripts, and experiments are provided for reproducibility.

---

## ⚠️ Notes

* Update dataset paths before running Java module
* Ensure Java and Python environments are properly configured
* GPU is optional but recommended for faster training

---

## 👨‍💻 Author

**Dr. TABUEU FOTSO Laurent Cabrel**
University of Bertoua / ENS
Researcher in Data Mining, AI, and Sequential Pattern Mining

---

## 📜 License

MIT License

---

## ⭐ Citation

```bibtex
@article{tabueu2026seqsgriteminerann,
title={A Hybrid Transformer Framework for Mining Gradual Sequential Patterns},
author={Tabueu Fotso, Laurent Cabrel},
journal={WIREs Data Mining and Knowledge Discovery},
year={2026}
}
```

