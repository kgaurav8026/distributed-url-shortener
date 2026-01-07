# Database Schema
# This file shows the database structure for the URL Shortener

# TABLE: urls
# +--------------+------------------+------+-----+
# | Field        | Type             | Null | Key |
# +--------------+------------------+------+-----+
# | id           | BIGINT           | NO   | PRI |
# | short_url    | VARCHAR(10)      | NO   | UNI |
# | long_url     | VARCHAR(2048)    | NO   |     |
# | created_at   | TIMESTAMP        | NO   |     |
# | expires_at   | TIMESTAMP        | YES  |     |
# | counter      | BIGINT           | NO   |     |
# +--------------+------------------+------+-----+

