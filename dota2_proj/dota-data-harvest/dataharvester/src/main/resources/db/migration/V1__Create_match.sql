CREATE TABLE IF NOT EXISTS match (
  id BIGINT NOT NULL UNIQUE PRIMARY KEY,
  first_blood_time int NULL,
  radiant_gold_advantage int[] null,
  radiant_experience_advantage int[] null,
  radiant_win boolean null,
  min_radiant_gold_advantage int null,
  max_radiant_gold_advantage int null,
  mean_radiant_gold_advantage decimal(10,5) null,
  std_dev_radiant_gold_advantage decimal(10,5) null,
  final_radiant_gold_advantage int null,
  min_radiant_experience_advantage int null,
  max_radiant_experience_advantage int null,
  mean_radiant_experience_advantage decimal(10,5) null,
  std_dev_radiant_experience_advantage decimal(10,5) null,
  final_radiant_experience_advantage int null
);
