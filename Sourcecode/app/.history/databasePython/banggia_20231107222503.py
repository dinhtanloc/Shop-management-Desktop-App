import csv
product_lst = [
"Thanh Son Cactus",
"Bird's Egg Cactus",
"Dragon Fruit Cactus",
"Birthday Cake Cactus",
"Mixed Cactus ",
"Bunny Ear Cactus",
"Lucky Leaves",
"Mixed Lucky Leaves",
"Prosperity",
"Triple Lucky Bamboo",
"Lucky Bamboo",
"Money Tree",
"Sansevieria",
"Small Dollar Tree",
"Large Dollar Tree",
"Jade Plant",
"Crown of Thorns",
"Green Striped",
 "Peacock Tail Plant",
"Five-Leaf Clovers",
"Longevity Plant",
" Dendrobium Orchid",
 "Happiness",
 "Lucky Mixed Cactus",
 "Rabbit Lotus Mix",
 "Cactus Stone Mix",
 "Lucky Knot Mix",
 "Lucky Stems Mix",
 "Lucky Stone Mix",
 "Chalkboard Accessories",
 "Animals",
 "Small Mushrooms",
 "Human-Shaped Mushrooms",
"Fences",
"Insects",
"Chickens"
]
data_price={}
data=[]
stt_sp=[i for i in range(1,len(product_lst)+1)]
type_sp=['Cactus' for _ in range(6)]+['Leaf' for _ in range(10)]+['Plant' for _ in range(2)]+['Peacock' for _ in range(2) ]+['Special' for _ in range(4)]+['Cactus']+['Stone Lotus' for _ in range(2)]+['Lucky Leaf' for _ in range(3)]+['Acessory' for _ in range(7)]
lst_price=[95,95,95,95,190,95,105,190,190,200,250,190,180,190,280,180,220,200,220,190,190,190,270,265,235,265,305,285,265,7,20,8,15,8,2,3]
# print(len(lst_price))
for i in range(len(product_lst)):
    # print(i)
    data_price[stt_sp[i]]=lst_price[i]*1000
    data.append([stt_sp[i],type_sp[i],product_lst[i],data_price[stt_sp[i]]])
# print(data)
    # Mở tệp CSV để ghi dữ liệu
with open('danhmucsp.csv', mode='w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    
    # Ghi tiêu đề nếu cần
    writer.writerow(['ProductID','Type', 'Description', 'UnitCost'])
    
    # Ghi nhiều hàng dữ liệu cùng một lúc
    writer.writerows(data)
    
