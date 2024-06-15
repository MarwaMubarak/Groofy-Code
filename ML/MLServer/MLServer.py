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
    return jsonify({'prediction': prediction.tolist()})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
