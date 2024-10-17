from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression, Ridge, Lasso, ElasticNet
from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
from sklearn.neural_network import MLPRegressor
from sklearn.svm import SVR
from sklearn.metrics import mean_squared_error, r2_score
import pandas as pd
import gc
import pickle

DIMENSIONS = 1024

gc.collect()

df = pd.read_parquet("./output_embeddings.parquet")

X = df[[f"d{i}" for i in range(0, DIMENSIONS)]]
y = df["target"]

df = None
gc.collect()

# Assume 'X' is your dataframe of embeddings and 'y' is your target variable
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

X = None
y = None
gc.collect()


# Train and evaluate each regressor
regressors = {
    "Linear Regression": LinearRegression(),
    "Ridge Regression": Ridge(),
    "Lasso Regression": Lasso(),
    "Elastic Net Regression": ElasticNet(),
    "Random Forest Regression": RandomForestRegressor(),
    "Gradient Boosting Regression": GradientBoostingRegressor(),
    "Support Vector Regression": SVR(),
    "MLP Regressor [512, 256]": MLPRegressor(
        hidden_layer_sizes=(512, 256), max_iter=1000, random_state=42
    ),
    "MLP Regressor [1024, 512, 256]": MLPRegressor(
        hidden_layer_sizes=(1024, 512, 256), max_iter=1000, random_state=42
    ),
    "MLP Regressor [1024, 512]": MLPRegressor(
        hidden_layer_sizes=(1024, 512), max_iter=1000, random_state=42
    ),
    "MLP Regressor [512, 256] (tanh)": MLPRegressor(
        hidden_layer_sizes=(512, 256), max_iter=1000, random_state=42, activation="tanh"
    ),
    "MLP Regressor [1024, 512, 256] (tanh)": MLPRegressor(
        hidden_layer_sizes=(1024, 512, 256),
        max_iter=1000,
        random_state=42,
        activation="tanh",
    ),
    "MLP Regressor [1024, 512] (tanh)": MLPRegressor(
        hidden_layer_sizes=(1024, 512),
        max_iter=1000,
        random_state=42,
        activation="tanh",
    ),
}

for name, regressor in regressors.items():
    regressor.fit(X_train, y_train)

    with open(f"./models/{name}.pickle", "wb") as f:
        pickle.dump(regressor, f)

    y_pred = regressor.predict(X_test)
    mse = mean_squared_error(y_test, y_pred)
    r2 = r2_score(y_test, y_pred)
    print(f"{name}: MSE = {mse:.2f}, R2 = {r2:.2f}")


# Choosing the optimal number and size of hidden layers for a neural network regressor depends on several factors, including the complexity of the problem, the size of the dataset, and the nature of the data. Here are some general guidelines for your dataset:
#
#     Number of hidden layers: 2-3 hidden layers are often sufficient for most problems. More layers can lead to overfitting, especially when the dataset is not extremely large.
#     Size of hidden layers: A common approach is to start with a larger number of neurons in the first hidden layer and gradually decrease the number of neurons in subsequent layers. This is known as a "funnel" architecture.
#
# For your dataset with 1024 features and 64k rows, here are some possible configurations:
#
#     Configuration 1: 2 hidden layers with 512 and 256 neurons, respectively.
#     Configuration 2: 3 hidden layers with 1024, 512, and 256 neurons, respectively.
#     Configuration 3: 2 hidden layers with 1024 and 512 neurons, respectively.
#
# You can start with one of these configurations and adjust the number and size of hidden layers based on the performance of the model.
#         - llama 3.1 70b
