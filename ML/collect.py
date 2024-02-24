import requests
import pandas as pd
import time


def make_request(url):
    max_retries = 100000
    retries = 0
    while retries < max_retries:
        response = requests.get(url)
        if response.status_code == 200:
            return response.json()['result']
        else:
            print(f"Failed to retrieve data. Retrying in 2 seconds...")
            time.sleep(2)
            retries += 1
    print(f"Failed to retrieve data after {max_retries} retries.")
    return None


def get_all_users():
    url = f"https://codeforces.com/api/user.ratedList?activeOnly=false&includeRetired=false"
    return make_request(url)


def get_all_problems():
    url = f"https://codeforces.com/api/problemset.problems"
    return make_request(url)


def get_user_stats(user_handle):
    url = f"https://codeforces.com/api/user.rating?handle={user_handle}"
    return make_request(url)


def get_user_submissions(user_handle):
    url = f"https://codeforces.com/api/user.status?handle={user_handle}"
    return make_request(url)


def main():
    print(f"Creating dataset...")
    data = [["user_handle","user_rating",
             "user_max_rating",
             "wins",
             "draws",
             "losses",
             "rate_800_cnt",
             "rate_900_cnt",
             "rate_1000_cnt",
             "rate_1100_cnt",
             "rate_1200_cnt",
             "rate_1300_cnt",
             "rate_1400_cnt",
             "rate_1500_cnt",
             "rate_1600_cnt",
             "rate_1700_cnt",
             "rate_1800_cnt",
             "rate_1900_cnt",
             "rate_2000_cnt",
             "rate_2100_cnt",
             "rate_2200_cnt",
             "rate_2300_cnt",
             "rate_2400_cnt",
             "rate_2500_cnt",
             "rate_2600_cnt",
             "rate_2700_cnt",
             "rate_2800_cnt",
             "rate_2900_cnt",
             "rate_3000_cnt",
             "rate_3100_cnt",
             "rate_3200_cnt",
             "rate_3300_cnt",
             "rate_3400_cnt",
             "rate_3500_cnt"]]

    users = get_all_users()
    print(f"Got all users...")
    # problems = get_all_problems()
    # print(f"Got all problems...")
    
    ok = False
    # ok = True
    for user in users:
        if user['handle'] == 'TeaTime':
            ok = True
        if ok:
            try:
                print(f"Progress: {100 * users.index(user)//len(users)}% complete. ({users.index(user)}/{len(users)})")
                print(f"Processing user {user['handle']}...")

                user_stats = get_user_stats(user['handle'])
                wins, draws, losses = 0, 0, 0
                for stat in user_stats:
                    if abs(stat['newRating'] - stat['oldRating']) <= 10:
                        draws += 1
                    elif stat['newRating'] > stat['oldRating']:
                        wins += 1
                    else:
                        losses += 1
                print(f"Got user wins, draws, losses...")

                user_submissions = get_user_submissions(user['handle'])

                df = pd.DataFrame(user_submissions)

                filtered_submissions = df[(df['problem'].apply(lambda x: 'rating' in x and x['rating'] is not None)) & (df['verdict'] == 'OK')]
                filtered_submissions = filtered_submissions.drop_duplicates(subset=['problem'])

                rating_counts = filtered_submissions[(filtered_submissions['problem'].apply(lambda x: 800 <= x['rating'] <= 3500))]['problem'].apply(lambda x: x['rating']).value_counts().sort_index()
                rating_counts_list = rating_counts.tolist()
                print(f"Got user rates...")

                data.append([user['handle'], user['rating'], user['maxRating'], wins, draws, losses] + rating_counts_list)
            except Exception as e:
                df = pd.DataFrame(data)
                df.to_excel('coders_dataset_2.xlsx', index=False)
                print(f"Failed to process user {user['handle']}. Reason: {e}")
                print("User Handle: ", user['handle'])
                exit()
            
    print(f"Creating excel file...")
    df = pd.DataFrame(data)
    df.to_excel('coders_dataset_2.xlsx', index=False)
    print(f"Done!")


if __name__ == "__main__":
    main()
