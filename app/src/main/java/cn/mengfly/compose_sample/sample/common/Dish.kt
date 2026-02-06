package cn.mengfly.compose_sample.sample.common

import androidx.annotation.DrawableRes
import cn.mengfly.compose_sample.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Dish(
    // 菜品ID
    val id: Int,
    // 名称
    val name: String,
    // 配料表
    val ingredients: List<String>,
    //  价格
    val price: Int = 0,
    // 描述
    val description: String = "",
    // 缩略图
    @param:DrawableRes
    val thumbnail: Int
)

object DishManager {

    /**
     * 菜品列表
     */
    val dishes = listOf(
        Dish(
            id = 1,
            name = "红烧肉",
            ingredients = listOf("五花肉", "生抽", "老抽", "冰糖", "料酒", "姜片", "葱段"),
            price = 38,
            description = "经典家常菜，肥而不腻，入口即化",
            thumbnail = R.drawable.ic_pork
        ),
        Dish(
            id = 2,
            name = "清蒸鲈鱼",
            ingredients = listOf("鲈鱼", "姜丝", "葱丝", "蒸鱼豉油", "料酒"),
            price = 45,
            description = "鲜嫩鲈鱼清蒸而成，保留原汁原味，营养丰富",
            thumbnail = R.drawable.ic_fish
        ),
        Dish(
            id = 3,
            name = "宫保鸡丁",
            ingredients = listOf(
                "鸡胸肉",
                "花生米",
                "干辣椒",
                "花椒",
                "葱段",
                "姜片",
                "蒜片",
                "酱油",
                "醋",
                "糖"
            ),
            price = 32,
            description = "川菜经典，鸡肉嫩滑，花生香脆，酸甜微辣",
            thumbnail = R.drawable.ic_chicken
        ),
        Dish(
            id = 4,
            name = "麻婆豆腐",
            ingredients = listOf(
                "嫩豆腐",
                "牛肉末",
                "豆瓣酱",
                "花椒",
                "蒜末",
                "葱花",
                "生抽",
                "老抽"
            ),
            price = 26,
            description = "麻辣鲜香，豆腐嫩滑，下饭神器",
            thumbnail = R.drawable.ic_tofu
        ),
        Dish(
            id = 5,
            name = "糖醋里脊",
            ingredients = listOf("猪里脊肉", "番茄酱", "白糖", "白醋", "淀粉", "鸡蛋", "面粉"),
            price = 35,
            description = "外酥内嫩，酸甜可口，老少皆宜",
            thumbnail = R.drawable.ic_sweet_sour
        ),
        Dish(
            id = 6,
            name = "水煮牛肉",
            ingredients = listOf(
                "牛肉片",
                "豆芽",
                "白菜",
                "干辣椒",
                "花椒",
                "豆瓣酱",
                "蒜末",
                "姜片"
            ),
            price = 42,
            description = "麻辣过瘾，牛肉滑嫩，汤汁浓郁",
            thumbnail = R.drawable.ic_beef
        ),
        Dish(
            id = 7,
            name = "回锅肉",
            ingredients = listOf("五花肉", "青椒", "蒜苗", "豆瓣酱", "甜面酱", "生抽", "老抽"),
            price = 30,
            description = "肥瘦相间，香气扑鼻，口感丰富",
            thumbnail = R.drawable.ic_pork
        ),
        Dish(
            id = 8,
            name = "鱼香肉丝",
            ingredients = listOf(
                "猪里脊肉",
                "胡萝卜",
                "木耳",
                "青椒",
                "蒜末",
                "姜末",
                "豆瓣酱",
                "醋",
                "糖"
            ),
            price = 28,
            description = "鱼香味浓，肉丝嫩滑，配菜爽脆",
            thumbnail = R.drawable.ic_fish
        ),
        Dish(
            id = 9,
            name = "酸菜鱼",
            ingredients = listOf("草鱼", "酸菜", "泡椒", "姜片", "蒜片", "花椒", "干辣椒"),
            price = 48,
            description = "酸辣开胃，鱼肉鲜嫩，汤汁酸爽",
            thumbnail = R.drawable.ic_pickled_fish
        ),
        Dish(
            id = 10,
            name = "东坡肉",
            ingredients = listOf("五花肉", "绍兴黄酒", "冰糖", "生抽", "老抽", "姜片", "葱段"),
            price = 58,
            description = "色泽红亮，肥而不腻，入口即化",
            thumbnail = R.drawable.ic_dongpo
        ),
        Dish(
            id = 11,
            name = "白切鸡",
            ingredients = listOf("三黄鸡", "姜片", "葱段", "料酒", "盐"),
            price = 36,
            description = "皮爽肉滑，原汁原味，蘸料提鲜",
            thumbnail = R.drawable.ic_white_cut
        ),
        Dish(
            id = 12,
            name = "口水鸡",
            ingredients = listOf(
                "鸡腿肉",
                "花生米",
                "芝麻",
                "辣椒油",
                "花椒粉",
                "蒜泥",
                "生抽",
                "醋"
            ),
            price = 29,
            description = "麻辣鲜香，鸡肉嫩滑，令人垂涎",
            thumbnail = R.drawable.ic_white_cut
        )
    )

    /**
     * 根据ID获取菜品
     */
    suspend fun getDishById(id: Int): Dish? {
        return withContext(Dispatchers.IO) {
            return@withContext dishes.firstOrNull { it.id == id }
        }
    }
}
