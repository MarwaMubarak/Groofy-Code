# from flask import Flask, request, jsonify
# import joblib
# import pandas as pd
#
# app = Flask(__name__)
#
# # Load the GMM model and the scaler
# try:
#     gmm_model = joblib.load('MLServer/gmm_model.pkl')
#     scaler = joblib.load('MLServer/scaler.pkl')
# except FileNotFoundError as e:
#     print(f"File not found: {e}")
#     gmm_model = None
#     scaler = None
#
# # Define cluster ratings based on the actual outputs of your clustering analysis
# cluster_ratings = {
#     0: 900,  1: 1100,  2: 1300,  3: 1500,  4: 1700,
#     5: 1900,  6: 2100,  7: 2300,  8: 2500,  9: 2700
# }
#
# # Define difficulty weights as used in the model training
# difficulty_weights = {
#     800: 0.8, 900: 0.9, 1000: 1.0, 1100: 1.1, 1200: 1.2, 1300: 1.3,
#     1400: 1.4, 1500: 1.5, 1600: 1.6, 1700: 1.7, 1800: 1.8, 1900: 1.9,
#     2000: 2.0, 2100: 2.1, 2200: 2.2, 2300: 2.3, 2400: 2.4, 2500: 2.5,
#     2600: 2.6, 2700: 2.7, 2800: 2.8, 2900: 2.9, 3000: 3.0, 3100: 3.1,
#     3200: 3.2, 3300: 3.3, 3400: 3.4, 3500: 3.5
# }
#
# @app.route('/predict', methods=['POST'])
# def predict():
#     if not gmm_model or not scaler:
#         return jsonify({'error': 'Model or scaler not loaded properly.'}), 500
#
#     data = request.get_json()  # This assumes JSON data which includes the features
#
#     # Prepare features DataFrame from the JSON data
#     features_df = pd.DataFrame([data])
#
#     # Calculate weighted and average difficulty features as per the model training
#     for rate, weight in difficulty_weights.items():
#         features_df[f'weighted_rate_{rate}_cnt'] = features_df.get(f'rate_{rate}_cnt', 0) * weight
#
#     features_df['average_difficulty'] = features_df.apply(
#         lambda row: sum(row[f'weighted_rate_{rate}_cnt'] for rate in difficulty_weights) /
#                     sum(row[f'rate_{rate}_cnt'] for rate in difficulty_weights if row[f'rate_{rate}_cnt'] > 0),
#         axis=1
#     )
#
#     # Ensure all expected features are present
#     expected_features = [f'weighted_rate_{rate}_cnt' for rate in difficulty_weights] + ['average_difficulty']
#     for feature in expected_features:
#         if feature not in features_df.columns:
#             features_df[feature] = 0  # Ensure every expected feature is in the DataFrame, filling missing with 0
#
#     # Scale the features
#     scaled_features = scaler.transform(features_df)
#
#     # Predict the cluster
#     cluster = gmm_model.predict(scaled_features)[0]
#
#     # Map the cluster to the expected rating
#     expected_rating = cluster_ratings.get(cluster, "Unknown cluster")
#
#     return jsonify({'expected_rating': expected_rating})
#
# if __name__ == '__main__':
#     app.run(host='0.0.0.0', port=5000)


from flask import Flask, request, jsonify
import joblib

app = Flask(__name__)
# Try to load the model, handle error if not found
try:
    model = joblib.load('MLServer/rating_prediction_rf_model.pkl')
except FileNotFoundError:
    print("Model file not found. Please check the file path.")
    model = None

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()  # This assumes JSON data which includes the features

    # Extract features from the JSON data
    features = [
        data['user_rating'],
        data['user_max_rating'],
        data['wins'],
        data['draws'],
        data['losses'],
        data['rate_800_cnt'],
        data['rate_900_cnt'],
        data['rate_1000_cnt'],
        data['rate_1100_cnt'],
        data['rate_1200_cnt'],
        data['rate_1300_cnt'],
        data['rate_1400_cnt'],
        data['rate_1500_cnt'],
        data['rate_1600_cnt'],
        data['rate_1700_cnt'],
        data['rate_1800_cnt'],
        data['rate_1900_cnt'],
        data['rate_2000_cnt'],
        data['rate_2100_cnt'],
        data['rate_2200_cnt'],
        data['rate_2300_cnt'],
        data['rate_2400_cnt'],
        data['rate_2500_cnt'],
        data['rate_2600_cnt'],
        data['rate_2700_cnt'],
        data['rate_2800_cnt'],
        data['rate_2900_cnt'],
        data['rate_3000_cnt'],
        data['rate_3100_cnt'],
        data['rate_3200_cnt'],
        data['rate_3300_cnt'],
        data['rate_3400_cnt'],
        data['rate_3500_cnt']
    ]

    prediction = model.predict([features])
    return jsonify({'expected_rating': prediction.tolist()[0]})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
