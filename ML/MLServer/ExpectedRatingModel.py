import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error, r2_score
import matplotlib.pyplot as plt
import joblib


def calculate_expected_rating(df):
    MIN_DELTA, MAX_DELTA = -200, 700
    difficulty_weights = {
        800: 0.5, 900: 0.6, 1000: 0.7, 1100: 0.8, 1200: 0.9,
        1300: 1.0, 1400: 1.1, 1500: 1.2, 1600: 1.3, 1700: 1.4,
        1800: 1.5, 1900: 1.6, 2000: 1.7, 2100: 1.8, 2200: 1.9,
        2300: 2.0, 2400: 2.1, 2500: 2.2, 2600: 2.3, 2700: 2.4,
        2800: 2.5, 2900: 2.6, 3000: 2.7, 3100: 2.8, 3200: 2.9,
        3300: 3.0, 3400: 3.1, 3500: 3.2
    }
    def calculate_performance_score(row):
        score = 0
        for rating in range(800, 3501, 100):
            column_name = f'rate_{rating}_cnt'
            if column_name in row:
                score += row[column_name] * difficulty_weights[rating]
        return score

    performance_scores = df.apply(calculate_performance_score, axis=1)

    # Incorporate wins, losses, draws, and max rating into the performance score
    adjusted_scores = performance_scores * (1 + df['wins'] / (df['losses'] + 1)) + df['draws'] * 0.1
    max_rating_influence = df['user_max_rating'] / 3500  # Normalizing the max rating influence

    # Add max rating influence to the adjusted scores
    adjusted_scores += adjusted_scores * max_rating_influence

    min_perf, max_perf = adjusted_scores.min(), adjusted_scores.max()
    normalized_performance_scores = (adjusted_scores - min_perf) / (max_perf - min_perf) * (MAX_DELTA - MIN_DELTA) + MIN_DELTA

    df['expected_rating'] = df['user_rating'] + normalized_performance_scores.clip(lower=MIN_DELTA, upper=MAX_DELTA)
    return df


# Load multiple datasets
all_dfs = []
for i in range(1, 27):  # Assuming datasets are numbered from 1 to 26
    file_path = f'coders_dataset_{i}.xlsx'
    df = pd.read_excel(file_path)
    df.fillna(0, inplace=True)  # Replace all NaN values with 0
    df = calculate_expected_rating(df)
    all_dfs.append(df)

# Combine all DataFrames into one
combined_df = pd.concat(all_dfs, ignore_index=True)

# Drop the 'user_handle' column
combined_df.drop(columns=['user_handle'], inplace=True)

combined_df.fillna(0, inplace=True)

# Define features and target
X = combined_df.drop('expected_rating', axis=1)
y = combined_df['expected_rating']

# Split the data
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Initialize and train the Random Forest model
model = RandomForestRegressor(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

# Predict and evaluate
y_pred = model.predict(X_test)
mse = mean_squared_error(y_test, y_pred)
r2 = r2_score(y_test, y_pred)
r2_percentage = r2 * 100

# Print evaluation metrics
print(f"Mean Squared Error: {mse}")
print(f"R-squared Percentage: {r2_percentage:.2f}%")

# Plot actual vs predicted
plt.scatter(y_test, y_pred, alpha=0.3)
plt.plot([y.min(), y.max()], [y.min(), y.max()], 'k--', lw=4)
plt.xlabel('Actual')
plt.ylabel('Predicted')
plt.title('Actual vs Predicted')
plt.show()

# Save the model
joblib.dump(model, 'rating_prediction_rf_model.pkl')
