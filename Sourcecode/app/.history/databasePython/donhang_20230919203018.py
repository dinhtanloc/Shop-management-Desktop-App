import csv
import random as rd
import datetime
start_date = datetime.date(year=2023, month=1, day=1)
end_date = datetime.date(year=2023, month=10, day=26)
date_list = [start_date + datetime.timedelta(days=i) for i in range((end_date - start_date).days + 1) for _ in range(rd.choice(range(10)))]
x=len(date_list)

data_price={}
lst_price=[95,95,95,95,190,95,105,190,190,180,200,250,190,180,190,280,180,220,200,220,190,190,190,270,265,235,265,305,285,265,7,20,8,15,8,2,3]

for i in range(37):
    # print(i)
    data_price[i+1]=lst_price[i]*1000

first_number=['090','028','084']
second_number=['332','664','321','556','234','385','567','992']
third_number=['198','341','236','756','810','365','773','749','885','931']
first_name=['Dinh','Nguyen','Le','Tran','Duong','Dao','Bui']
last_name=['Tan Sang','Van Tuan','Ba Thanh','Tuan Ky','Van Phat','Thanh Huong','Kim Chi']
data_OrderID=[i for i in range(x)]
data_ProductID=[rd.choice(range(1,38)) for i in range(x)]
data_Quantity=[rd.choice(range(1,20)) for i in range(x)]
data_OrderDate=[date for date in date_list]
data_Name=[rd.choice(first_name)+' '+rd.choice(last_name) for i in range(x)]
data_PhoneNumber=['(84+)'+rd.choice(first_number)+rd.choice(second_number)+rd.choice(third_number) for i in range(x)]
data_saleperson=[rd.choice(['Loc Dinh','Tuan Anh']) for i in range(x)]
# for i in range(x):
#     print(f"{data_OrderID[i]}-{data_ProductID[i]}-{data_Quantity[i]}-{data_OrderDate[i]}-{data_Name[i]}-{data_PhoneNumber[i]}-{data_saleperson[i]}".encode('utf-8'))


with open('donhang.csv', mode='w', newline='',encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(['OrderID','OrderDate','ProductID','Quantity','Unitcost','Totalcost','Name', 'PhoneNumber','Saleperson'])
    for i in range(x):
        writer.writerow([data_OrderID[i],data_OrderDate[i],data_ProductID[i],data_Quantity[i],data_price[data_ProductID[i]],data_price[data_ProductID[i]]*data_Quantity[i],data_Name[i],data_PhoneNumber[i],data_saleperson[i]])
