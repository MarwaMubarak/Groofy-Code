# Sample weights for different attributes
weight_current_rating = 0.4
weight_solved_problems = 0.4
weight_max_rating = 0.2

# Calculate user proficiency score
total_solved_problems = count_800_problems + count_900_problems + ... + count_3500_problems

if total_solved_problems == 0:
    average_solved_rating = 0
else:
    average_solved_rating = (
        (count_800_problems * 800 + count_900_problems * 900 + ... + count_3500_problems * 3500) /
        total_solved_problems
    )

user_proficiency_score = (
    weight_current_rating * (user_current_rating / 3500) + 
    weight_solved_problems * (average_solved_rating / 3500) + 
    weight_max_rating * (user_max_rating / 3500)
)

# Determine suitable problem rating based on user proficiency score
# You can define your own logic here based on your application's requirements
# For example, you can set thresholds to determine which problem rating range is suitable for the user
