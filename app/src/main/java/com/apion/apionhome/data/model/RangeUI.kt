package com.apion.apionhome.data.model

object RangeUI {

    val priceRangeUis = mapOf(
        "price" to "Mức giá",
        "lessThan500" to "< 500 triệu",
        "from500to800" to "500 - 800 triệu",
        "from800to1ty" to "800 triệu - 1 tỷ",
        "from1to2ty" to "1 - 2 tỷ",
        "from2to3ty" to "2 - 3 tỷ",
        "from3to5ty" to "3 - 5 tỷ",
        "from5to7ty" to "5 - 7 tỷ",
        "from7to10ty" to "7 - 10 tỷ",
        "from10to20ty" to "10 - 20 tỷ",
        "from20to30ty" to "20 - 30 tỷ",
        "over30ty" to "> 30 tỷ",
        )

    val acreageRangeUis = mapOf(
        "acreage" to "Diện tích",
//        "notDetermine" to "Chưa xác định",
        "lesThan30m2" to "< 30 m2",
        "from30to50m2" to "30 - 50 m2",
        "from50to80m2" to "50 - 80 m2",
        "from80to100m2" to "80 - 100 m2",
        "from100to150m2" to "100 - 150 m2",
        "from150to200m2" to "150 - 200 m2",
        "from200to250m2" to "200 - 250 m2",
        "from250to300m2" to "250 - 300 m2",
        "from300to500m2" to "300 - 500 m2",
        "over500m2" to "> 500 m2",
        )

    val frontWidthRangeUis = mapOf(
        "frontWidth" to "Mặt tiền",
        "lessThan3m" to "< 3 m",
        "from3to5m" to "3 - 5 m",
        "from5to7m" to "5 - 7 m",
        "from7to9" to "7 - 9 m",
        "from9to11m" to "9 - 11 m",
        "from11to13m" to "11 - 13 m",
        "from13to15m" to "13 - 15 m",
        "over15m" to "> 15 m",
        )

    val homeDirectionRangeUis = mapOf(
        "Direction" to "Hướng nhà",
        "Not Determine" to "Không xác định",
        "East" to "Đông",
        "West" to "Tây",
        "South" to "Nam",
        "North" to "Bắc",
        "North - East" to "Đông - Bắc",
        "North - West" to "Tây - Bắc",
        "South - East" to "Đông - Nam",
        "South - West" to "Tây - Nam",
        )

    val houseTypeRangeUis = mapOf(
        "" to "Tất cả",
        "House" to "Nhà đất",
        "Apartment" to "Căn hộ chung cư",
        "Villas" to "Nhà biệt thự, liền kề",
        "Downtown House" to "Nhà mặt phố",
        "Project Land" to "Đất nền",
        "Sell Land" to "Nhà mặt phố",
        "Project Land" to "Đất nền",
        "Farm and Resort" to "Trang trại, khu nghỉ dưỡng",
        "Warehouse and Factory" to "Kho, nhà xưởng",
        "Others" to "Bất động sản khác"
        )
    val jobUis = mapOf(
        "Looking for a job" to "Đang tìm việc",
        "Had a job" to "Đang làm công việc khác"
    )
    val sexUis = mapOf(
        "Male" to "Nam",
        "Female" to "Nữ"
    )
    val levelUis = mapOf(
        "University" to "Đại học",
        "College" to "Cao đẳng",
        "Vocational School" to "Trung cấp",
        "High School Graduation" to "Trung học phổ thông"
    )
    val bedroomUis = mapOf(
        0 to "Số phòng ngủ",
        1 to "1+",
        2 to "2+",
        3 to "3+",
        4 to "4+",
        5 to "5+",
    )
}
