import requests
import pandas as pd


def get_recent_submissions():
    url = f"https://codeforces.com/api/problemset.recentStatus?count=5"
    response = requests.get(url)
    if response.status_code == 200:
        submissions = response.json()['result']
        return submissions
    else:
        print("Failed to retrieve submissions.")
        return None


def get_user_rate(user_handle):
    url = f"https://codeforces.com/api/user.info?handles={user_handle}&checkHistoricHandles=true"
    response = requests.get(url)
    if response.status_code == 200:
        user_info = response.json()['result'][0]
        if 'rating' in user_info:
            rate = max(user_info['rating'], user_info['maxRating'])
        else:
            rate = 0
        return rate
    else:
        print(f"Failed to retrieve user info")
        return None

def get_all_users():
    url = f"https://codeforces.com/api/user.ratedList?activeOnly=false&includeRetired=false"
    response = requests.get(url)
    if response.status_code == 200:
        return len(response.json()['result'])
    else:
        print(f"Failed to retrieve all users")
        return None


def count_wrong_attempts(contest_id, user_handle, problem_idx):
    url = f"https://codeforces.com/api/contest.status?contestId={contest_id}&handle={user_handle}&from=1&count=100000"
    response = requests.get(url)
    if response.status_code == 200:
        attempts = response.json()['result']
        wrong_cnt = 0
        solved = False
        for attempt in attempts:
            if attempt['problem']['index'] == problem_idx:
                if attempt['verdict'] != 'OK':
                    wrong_cnt += 1
                else:
                    solved = True
        return solved, wrong_cnt
    else:
        print("Failed to retrieve user contest status")
        return None, None


def main():
    # print(f"Creating dataset...")
    # submissions = get_recent_submissions()
    # data = []
    # for submission in submissions:
    #     if 'rating' not in submission['problem']:
    #         continue
    #     problem_data = submission['problem']
    #     user_handle = submission['author']['members'][0]['handle']
    #     user_rate = get_user_rate(user_handle)
    #     problem_tags = ', '.join(problem_data['tags'])
    #     solved, wrong_cnt = count_wrong_attempts(problem_data['contestId'], user_handle, problem_data['index'])
    #     data.append({'User_Rate': user_rate, 'Problem_Rate': problem_data['rating'], 'Problem_Tags': problem_tags,
    #                  'Solved': solved, 'Wrong_Attempts': wrong_cnt})
    # print(f"Creating excel file...")
    # df = pd.DataFrame(data)
    # df.to_excel('coders_dataset.xlsx', index=False)
    # print(f"Done!")
    print(get_all_users())


if __name__ == "__main__":
    main()
