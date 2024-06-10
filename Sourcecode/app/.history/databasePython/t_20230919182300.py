import datetime
import random as rd

# Ngày bắt đầu (1 tháng 9)
start_date = datetime.date(year=2023, month=9, day=1)

# Ngày kết thúc (31 tháng 12)
end_date = datetime.date(year=2023, month=9, day=30)

# Tạo danh sách các ngày lặp lại từ 1 đến 9 lần
date_list = [(start_date + datetime.timedelta(days=i)) for i in range((end_date - start_date).days + 1) for _ in range(rd.choice(range(1, 10)))]

# In ra danh sách các ngày
for date in date_list:
    print(date)