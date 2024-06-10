import csv
import random as rd
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
stt_sp=[i for i in range(1,len(product_lst)+1)]
qty_sp=[rd.choice([rd.choice(range(50)),0]) for i in range(len(product_lst))]
data=[]
for i in range(len(product_lst)):
    data.append([stt_sp[i],qty_sp[i]])
with open('test.csv', mode='w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    
    # Ghi tiêu đề nếu cần
    writer.writerow(['ProductID', 'InventoryQty'])
    
    # Ghi nhiều hàng dữ liệu cùng một lúc
    writer.writerows(data)
