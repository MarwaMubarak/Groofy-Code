import numpy as np
import pandas as pd
from sklearn.mixture import GaussianMixture
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import PCA
from sklearn.metrics import silhouette_score
import joblib
import matplotlib.pyplot as plt

# Load multiple datasets
all_dfs = []
for i in range(1, 27):  # Assuming datasets are numbered from 1 to 26
    file_path = f'coders_dataset_{i}.xlsx'
    df = pd.read_excel(file_path)
    df.fillna(0, inplace=True)  # Replace all NaN values with 0
    all_dfs.append(df)

# Combine all DataFrames into one
combined_df = pd.concat(all_dfs, ignore_index=True)

# Drop the 'user_handle' column if it exists
if 'user_handle' in combined_df.columns:
    combined_df.drop(columns=['user_handle'], inplace=True)

# Define difficulty weights
difficulty_weights = {
    800: 0.8, 900: 0.9, 1000: 1.0, 1100: 1.1, 1200: 1.2, 1300: 1.3,
    1400: 1.4, 1500: 1.5, 1600: 1.6, 1700: 1.7, 1800: 1.8, 1900: 1.9,
    2000: 2.0, 2100: 2.1, 2200: 2.2, 2300: 2.3, 2400: 2.4, 2500: 2.5,
    2600: 2.6, 2700: 2.7, 2800: 2.8, 2900: 2.9, 3000: 3.0, 3100: 3.1,
    3200: 3.2, 3300: 3.3, 3400: 3.4, 3500: 3.5
}

# Calculate weighted rates
for rate, weight in difficulty_weights.items():
    combined_df[f'weighted_rate_{rate}_cnt'] = combined_df[f'rate_{rate}_cnt'] * weight

# Calculate the average problem difficulty for each row
def calculate_average_difficulty(row):
    total_weighted = sum(row[f'weighted_rate_{rate}_cnt'] for rate in difficulty_weights)
    total_problems = sum(row[f'rate_{rate}_cnt'] for rate in difficulty_weights)
    return total_weighted / total_problems if total_problems > 0 else 0

combined_df['average_difficulty'] = combined_df.apply(calculate_average_difficulty, axis=1)

# Normalize features
scaler = StandardScaler()
scaled_data = scaler.fit_transform(combined_df.select_dtypes(include=[np.number]))

# Train Gaussian Mixture Model
num_clusters = 28
gmm = GaussianMixture(n_components=num_clusters, random_state=42)
gmm.fit(scaled_data)

# Save the model and scaler
joblib.dump(gmm, 'gmm_model.pkl')
joblib.dump(scaler, 'scaler.pkl')

# Assign clusters and calculate cluster ratings
combined_df['cluster'] = gmm.predict(scaled_data)
cluster_ratings = combined_df.groupby('cluster')['user_rating'].mean().to_dict()

# Output the cluster ratings
print("Cluster Ratings:", cluster_ratings)

# Perform PCA to reduce dimensions for visualization
pca = PCA(n_components=2)
pca_data = pca.fit_transform(scaled_data)

# Scatter plot of the clusters
plt.figure(figsize=(10, 7))
for cluster in np.unique(combined_df['cluster']):
    plt.scatter(pca_data[combined_df['cluster'] == cluster, 0],
                pca_data[combined_df['cluster'] == cluster, 1],
                label=f'Cluster {cluster}')
plt.title('Clusters Visualization using PCA')
plt.xlabel('Principal Component 1')
plt.ylabel('Principal Component 2')
plt.legend()
plt.show()

# Calculate the silhouette score to evaluate clustering quality
sil_score = silhouette_score(scaled_data, combined_df['cluster'])
print(f'Silhouette Score: {sil_score:.2f}')
