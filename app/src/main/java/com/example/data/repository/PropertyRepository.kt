package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.AppointmentEntity
import com.example.data.local.SavedPropertyEntity
import com.example.data.model.AiAnalysis
import com.example.data.model.Appointment
import com.example.data.model.ChatMessage
import com.example.data.model.RoomProperty
import com.example.data.model.SoftWeight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class PropertyRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val appDao = database.appDao()

    companion object {
        val SAMPLE_PROPERTIES = listOf(
            RoomProperty(
                id = "p1",
                title = "Studio ban công thoáng, full nội thất ngõ 165 Cầu Giấy",
                price = 2800000,
                formattedPrice = "2.800.000 đ/tháng",
                estimatedTotalCost = "~3.250.000 đ",
                rating = 4.9,
                reviewCount = 34,
                area = "24m²",
                floor = "Tầng 3/6",
                bathroomType = "Khép kín",
                balcony = "Hướng Nam",
                distanceInfo = "Cách ĐH Sư Phạm 1.1km (~4 phút)",
                address = "Số 28, Ngõ 165 Cầu Giấy, P. Dịch Vọng, Q. Cầu Giấy, Hà Nội",
                matchPercentage = 96,
                isLandlordVerified = true,
                has360Tour = true,
                photoCount = 8,
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuATeEwiAdNjadJhptJZpSyrG6_MfCx2urQXIBew78AC7sdJ_HXyzEYpHJXWLwaRZfpUsqZTcyUMyU0O0j2VAx1ZkdZCd4vPCdUmfFcPgc8pUfcERfxuWmVxbH2HC0BNZO93J-YVM2mlLAk5iZ_52zpyxHm9HltF3GdOV8116UlLjpNq8-Av0r0jKW5FHOZZSAx9kkjmChcoWVVCiIwazsya904ibMrzDB4OZw-QmC3qxSPpr1UWpMgu",
                tourUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDgCLV4dpzlg_mfjowZym-JhMOGhYoqGMnVM5AG10kfBdiIhjo5IM2XoJh9M7s6rspts3ffQPXM75X-c3-py8uxncEKV4WxFPxCaeyNNnrc0hyTiQ-MJ6BncIuruDg4hkSpzlvPqcHMtzhbNGmwe7ONC2RKsBfmUz4MaePHAnfFGCUno_iE1ZCYGoxbkPd_qJGGQSNqN9vvyKyNLrd1VER2wQe7Mwb1IwdJu8HKgBpzDd2dNVdUUszx",
                floorplanUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC-aw2yl3lMrBOVQ_1IwGbg_Whp6xdu6vrSzMcWigTWIvE9Tbz7eJaeDTcibWEuXA7OTZA2kZCgGRBuNJBmcho4ZsofOgMhUdrcOqMe34lgU_o9TKtBPx6GtNJdL2t8UZ7Cr3kOQXKaWBRWTThldrzfZx4MzT3vlzB5SbsJU5Ic1DnDkMgivI4ttI4GNPRtJTrtOScE9qVo_pjDvudMwYQkgGAzVAd17c5VCkz6dNa-Pp1oqC61105R",
                aiReasons = listOf(
                    "Trong ngân sách bạn đã đặt (≤ 3.5tr trọn gói)",
                    "Đã trang bị điều hòa Inverter + WC riêng khép kín",
                    "Khu dân trí cao, chỉ số yên tĩnh đạt 9.2/10 theo GPS cảm biến"
                ),
                amenities = listOf("Điều hòa Inverter", "Nóng lạnh", "Giường nệm", "Tủ âm tường", "Chỗ để xe máy miễn phí"),
                landlordName = "Bác Nguyễn Văn Thành",
                landlordAvatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuAwfsW1657z_PHl909fRMQu6_GgQFkXQ3O6aSz0As0dzK0Afpqz6P5GHgD2Ejaa2IEIQSvOZ1t_UAQY8MHwESGaNsAj9EALubmqh2dTbiZTgff8c1yFNroxyjEx5AaldvYgHxwPITjdRNEpZBfNmTP_AwFbZZSHXnkSB1e_FjTnUAUgPv4GmOKjLKBtSBRJAjbG4p36xKUtRHBzZLeghcu6XQn0p3IyHxCo0ZZecSZMcQz1170Fe6_4",
                landlordExperience = "Chủ trọ 4 năm",
                landlordRating = 4.9,
                landlordReviews = 42,
                responseTime = "Dưới 5 phút",
                fixedRent = 2800000,
                electricityEstimate = 280000,
                waterEstimate = 90000,
                internetCost = 80000,
                serviceCost = 50000,
                parkingCost = 0,
                lat = 21.0360,
                lng = 105.7925,
                shortPriceLabel = "2.8tr"
            ),
            RoomProperty(
                id = "p2",
                title = "Căn hộ mini gác lửng cao cấp Dịch Vọng Hậu",
                price = 3200000,
                formattedPrice = "3.200.000 đ/tháng",
                estimatedTotalCost = "~3.650.000 đ",
                rating = 4.8,
                reviewCount = 19,
                area = "28m²",
                floor = "Tầng 2/5",
                bathroomType = "Khép kín",
                balcony = "Cửa sổ thoáng",
                distanceInfo = "Cách bạn 1.8km (~6 phút)",
                address = "Ngõ 45 Dịch Vọng Hậu, Q. Cầu Giấy, Hà Nội",
                matchPercentage = 91,
                isLandlordVerified = true,
                has360Tour = true,
                photoCount = 6,
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAei_Rvaww3_7xtw9dUl4YuXwfF_5u_Oqi1vxrVwzkQuBQ1DTrYTNI_LTJj0lug9evuWS726aaiNv_1AGZwRXdLXp_bY4J1Iv7naKmAQuxhVTPDnzTsVuB7Hquo7XcZ65NSdKfcpUEwOtuDwM2KOilAEbUuu9s9qvDt9hIZXgxOtLvVlUROYjR8I69LcpoQZ-PMQssHwFbHrBYC2ZcGyBX6EeU72T1OQ1xjxU6mMZOQpu8-Nnth3Tba",
                tourUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDgCLV4dpzlg_mfjowZym-JhMOGhYoqGMnVM5AG10kfBdiIhjo5IM2XoJh9M7s6rspts3ffQPXM75X-c3-py8uxncEKV4WxFPxCaeyNNnrc0hyTiQ-MJ6BncIuruDg4hkSpzlvPqcHMtzhbNGmwe7ONC2RKsBfmUz4MaePHAnfFGCUno_iE1ZCYGoxbkPd_qJGGQSNqN9vvyKyNLrd1VER2wQe7Mwb1IwdJu8HKgBpzDd2dNVdUUszx",
                floorplanUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC-aw2yl3lMrBOVQ_1IwGbg_Whp6xdu6vrSzMcWigTWIvE9Tbz7eJaeDTcibWEuXA7OTZA2kZCgGRBuNJBmcho4ZsofOgMhUdrcOqMe34lgU_o9TKtBPx6GtNJdL2t8UZ7Cr3kOQXKaWBRWTThldrzfZx4MzT3vlzB5SbsJU5Ic1DnDkMgivI4ttI4GNPRtJTrtOScE9qVo_pjDvudMwYQkgGAzVAd17c5VCkz6dNa-Pp1oqC61105R",
                aiReasons = listOf(
                    "Gần tuyến xe buýt số 26, 32 và tàu điện Nhổn",
                    "Tòa nhà có thang máy & bảo vệ trực 24/7 an ninh tuyệt đối",
                    "Tiết kiệm ~400k/tháng so với mặt bằng cùng phân khúc"
                ),
                amenities = listOf("Gác lửng gỗ cao", "Điều hòa", "Thang máy", "Bảo vệ 24/7", "Khóa vân tay"),
                landlordName = "Chị Thu Trang",
                landlordAvatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuCYpD_dR0LklB2coqesPODxOKPX7LxulbbFk_S-Ni8U4E9ueA2K5rpK7pvpYKBogK2e4n8A2JxNf3q2DkkaCPzhndMPeDigMogGZ4saiEL7nwsvNFZz4DqDSlVU4TlBFm9sKynbp_GSt5hlt1kaGLDCF1ebWi4mm5iTgsA_LUuZFJm7fg6O4QqzKceB2LPAaGBo-mSDfFlaZVRjN1amiS1Ss6bUXANMuVOeH_oSDC-C7mRD-33V7TGO",
                landlordExperience = "Chủ trọ 2 năm",
                landlordRating = 4.8,
                landlordReviews = 19,
                responseTime = "Dưới 10 phút",
                fixedRent = 3200000,
                electricityEstimate = 300000,
                waterEstimate = 80000,
                internetCost = 70000,
                serviceCost = 60000,
                parkingCost = 0,
                lat = 21.0330,
                lng = 105.7890,
                shortPriceLabel = "3.2tr",
                commuteNote = "6 phút đi xe máy",
                streetCondition = "Ngõ rộng 3m thông thoáng"
            ),
            RoomProperty(
                id = "p3",
                title = "Phòng trọ sinh viên khép kín ngõ Phan Văn Trường",
                price = 2200000,
                formattedPrice = "2.200.000 đ/tháng",
                estimatedTotalCost = "~2.600.000 đ",
                rating = 4.7,
                reviewCount = 15,
                area = "18m²",
                floor = "Tầng 2/4",
                bathroomType = "Khép kín",
                balcony = "Cửa sổ trời",
                distanceInfo = "0.8km từ bạn (~3 phút)",
                address = "Ngõ 12 Phan Văn Trường, Dịch Vọng Hậu, Cầu Giấy, Hà Nội",
                matchPercentage = 88,
                isLandlordVerified = true,
                has360Tour = false,
                photoCount = 5,
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBBntYjso4XUdIFRqxQtziuSeCL_FS6iQOdoM_K4SjaRoq_jOt1qmdQCmK2VcH63PXQ9rwHOcaGiJMUgZ5Db8kWEjEMjGfqCjDrPlEtsyxdeQ2HhdmVVt0Ua4cb7M_koTIFQk01QdIlU_yL135MhSqVH3hUJe3OZsNINsUljEgoqhPQ_lqP9NdqfwpAiuPNdBR1WJX2p7Cl3066y45ZKW_mbK-xxGrW8tU5gefwX8XLpuoFki3ed2YW",
                tourUrl = "",
                floorplanUrl = "",
                aiReasons = listOf(
                    "Giá rẻ nhất phân khúc, phù hợp sinh viên",
                    "Tự do giờ giấc, có chỗ nấu ăn riêng",
                    "Gần chợ Nhà Xanh và các trường ĐH lớn"
                ),
                amenities = listOf("WC riêng", "Bình nóng lạnh", "Wifi", "Chỗ để xe"),
                landlordName = "Cô Hoàng Nga",
                landlordAvatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuAwfsW1657z_PHl909fRMQu6_GgQFkXQ3O6aSz0As0dzK0Afpqz6P5GHgD2Ejaa2IEIQSvOZ1t_UAQY8MHwESGaNsAj9EALubmqh2dTbiZTgff8c1yFNroxyjEx5AaldvYgHxwPITjdRNEpZBfNmTP_AwFbZZSHXnkSB1e_FjTnUAUgPv4GmOKjLKBtSBRJAjbG4p36xKUtRHBzZLeghcu6XQn0p3IyHxCo0ZZecSZMcQz1170Fe6_4",
                landlordExperience = "Chủ trọ 6 năm",
                landlordRating = 4.7,
                landlordReviews = 28,
                responseTime = "Dưới 15 phút",
                fixedRent = 2200000,
                electricityEstimate = 200000,
                waterEstimate = 80000,
                internetCost = 60000,
                serviceCost = 40000,
                parkingCost = 0,
                lat = 21.0385,
                lng = 105.7875,
                shortPriceLabel = "2.1tr",
                commuteNote = "3 phút đi xe máy",
                streetCondition = "Cách mặt phố 30m"
            ),
            RoomProperty(
                id = "p4",
                title = "Phòng khép kín ngõ Chùa Láng",
                price = 2900000,
                formattedPrice = "2.900.000 đ/tháng",
                estimatedTotalCost = "~3.350.000 đ",
                rating = 4.9,
                reviewCount = 22,
                area = "22m²",
                floor = "Tầng 3/5",
                bathroomType = "Khép kín",
                balcony = "Cửa sổ rộng",
                distanceInfo = "650m đến FTU (3 phút đi bộ)",
                address = "Ngõ 80 Chùa Láng, P. Láng Thượng, Q. Đống Đa, Hà Nội",
                matchPercentage = 95,
                isLandlordVerified = true,
                has360Tour = true,
                photoCount = 9,
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBZSG_8GbQecKrjsl5nBphAf_MH0H7fuXufup08xw4-wFxzLkieNKX9dlB9eCNU1msmolR1I27-MxdO-_mnPR8coXguTz8kJ7YZgRGdW7CgM_keV4JsAv_y3NwWu2CMc-pkMCk41C4YcZ1akqjdmcceR_HSx6PNh9aZMGB3-ikol8-goSssRwj2sG1gf4g7px0r635KS2_VDKVjerppukGydHvHAPEybR7AubaWMLtHcFwWhUhrXiY0",
                tourUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDgCLV4dpzlg_mfjowZym-JhMOGhYoqGMnVM5AG10kfBdiIhjo5IM2XoJh9M7s6rspts3ffQPXM75X-c3-py8uxncEKV4WxFPxCaeyNNnrc0hyTiQ-MJ6BncIuruDg4hkSpzlvPqcHMtzhbNGmwe7ONC2RKsBfmUz4MaePHAnfFGCUno_iE1ZCYGoxbkPd_qJGGQSNqN9vvyKyNLrd1VER2wQe7Mwb1IwdJu8HKgBpzDd2dNVdUUszx",
                floorplanUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC-aw2yl3lMrBOVQ_1IwGbg_Whp6xdu6vrSzMcWigTWIvE9Tbz7eJaeDTcibWEuXA7OTZA2kZCgGRBuNJBmcho4ZsofOgMhUdrcOqMe34lgU_o9TKtBPx6GtNJdL2t8UZ7Cr3kOQXKaWBRWTThldrzfZx4MzT3vlzB5SbsJU5Ic1DnDkMgivI4ttI4GNPRtJTrtOScE9qVo_pjDvudMwYQkgGAzVAd17c5VCkz6dNa-Pp1oqC61105R",
                aiReasons = listOf(
                    "Đi bộ đến FTU chỉ 3 phút, ngõ yên tĩnh chuyên sinh viên",
                    "Đầy đủ máy lạnh Inverter & WC riêng có nóng lạnh",
                    "Nhà xe có camera và khóa vân tay 2 lớp an toàn"
                ),
                amenities = listOf("Điều hòa", "WC khép kín", "Nhà xe camera", "Khóa cửa vân tay", "Bàn học sinh viên"),
                landlordName = "Anh Tuấn Dũng",
                landlordAvatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuAwfsW1657z_PHl909fRMQu6_GgQFkXQ3O6aSz0As0dzK0Afpqz6P5GHgD2Ejaa2IEIQSvOZ1t_UAQY8MHwESGaNsAj9EALubmqh2dTbiZTgff8c1yFNroxyjEx5AaldvYgHxwPITjdRNEpZBfNmTP_AwFbZZSHXnkSB1e_FjTnUAUgPv4GmOKjLKBtSBRJAjbG4p36xKUtRHBzZLeghcu6XQn0p3IyHxCo0ZZecSZMcQz1170Fe6_4",
                landlordExperience = "Chủ trọ 3 năm",
                landlordRating = 4.9,
                landlordReviews = 31,
                responseTime = "Dưới 3 phút",
                fixedRent = 2900000,
                electricityEstimate = 260000,
                waterEstimate = 80000,
                internetCost = 80000,
                serviceCost = 50000,
                parkingCost = 0,
                lat = 21.0255,
                lng = 105.8020,
                shortPriceLabel = "2.9tr",
                commuteNote = "3 phút đi bộ đến FTU",
                streetCondition = "Ngõ cụt an ninh tuyệt đối"
            ),
            RoomProperty(
                id = "p5",
                title = "Phòng studio gác lửng Huỳnh Thúc Kháng",
                price = 3100000,
                formattedPrice = "3.100.000 đ/tháng",
                estimatedTotalCost = "~3.550.000 đ",
                rating = 4.8,
                reviewCount = 18,
                area = "25m²",
                floor = "Gác xép",
                bathroomType = "Khép kín",
                balcony = "Cửa sổ view hồ",
                distanceInfo = "Cách FTU 1.4km (5 phút xe máy)",
                address = "Ngõ 14 Huỳnh Thúc Kháng, P. Láng Hạ, Q. Ba Đình, Hà Nội",
                matchPercentage = 92,
                isLandlordVerified = true,
                has360Tour = true,
                photoCount = 7,
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDAUT99P6M4XEu7WJRXvVpHdveN6VJK0_Lr-NS63cuE-QmkDsta3b6TliyWg-heynTxgRXZZBgnfuLorHWY5krqjXahzWOCYEUgTurOJDRi0jBeeGhtHjRa-hXixd3SLEoaqmYB6U-2V0kzpuKIL1aJz6w9cTE60syFIkocRRk4c631SAfT5a0mjyTEq5I8ZkSEEEAcWIfc2YGCPrAE_XnU8CEI11506jh9AzCP54SjmVWeKtX-vRws",
                tourUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDgCLV4dpzlg_mfjowZym-JhMOGhYoqGMnVM5AG10kfBdiIhjo5IM2XoJh9M7s6rspts3ffQPXM75X-c3-py8uxncEKV4WxFPxCaeyNNnrc0hyTiQ-MJ6BncIuruDg4hkSpzlvPqcHMtzhbNGmwe7ONC2RKsBfmUz4MaePHAnfFGCUno_iE1ZCYGoxbkPd_qJGGQSNqN9vvyKyNLrd1VER2wQe7Mwb1IwdJu8HKgBpzDd2dNVdUUszx",
                floorplanUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC-aw2yl3lMrBOVQ_1IwGbg_Whp6xdu6vrSzMcWigTWIvE9Tbz7eJaeDTcibWEuXA7OTZA2kZCgGRBuNJBmcho4ZsofOgMhUdrcOqMe34lgU_o9TKtBPx6GtNJdL2t8UZ7Cr3kOQXKaWBRWTThldrzfZx4MzT3vlzB5SbsJU5Ic1DnDkMgivI4ttI4GNPRtJTrtOScE9qVo_pjDvudMwYQkgGAzVAd17c5VCkz6dNa-Pp1oqC61105R",
                aiReasons = listOf(
                    "Tòa nhà mới xây 2023, nội thất hiện đại cao cấp",
                    "Tầng 1 là hầm để xe rộng có khóa vân tay 2 lớp + camera 24/7",
                    "Cực kỳ bảo đảm tài sản xe máy cho sinh viên"
                ),
                amenities = listOf("Gác lửng thông minh", "Điều hòa 12000BTU", "Tủ lạnh mini", "Hầm gửi xe an toàn"),
                landlordName = "Bác Hoàng Văn Lâm",
                landlordAvatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuAwfsW1657z_PHl909fRMQu6_GgQFkXQ3O6aSz0As0dzK0Afpqz6P5GHgD2Ejaa2IEIQSvOZ1t_UAQY8MHwESGaNsAj9EALubmqh2dTbiZTgff8c1yFNroxyjEx5AaldvYgHxwPITjdRNEpZBfNmTP_AwFbZZSHXnkSB1e_FjTnUAUgPv4GmOKjLKBtSBRJAjbG4p36xKUtRHBzZLeghcu6XQn0p3IyHxCo0ZZecSZMcQz1170Fe6_4",
                landlordExperience = "Chủ trọ 5 năm",
                landlordRating = 4.8,
                landlordReviews = 27,
                responseTime = "Dưới 8 phút",
                fixedRent = 3100000,
                electricityEstimate = 280000,
                waterEstimate = 90000,
                internetCost = 80000,
                serviceCost = 50000,
                parkingCost = 0,
                lat = 21.0200,
                lng = 105.8110,
                shortPriceLabel = "3.1tr",
                commuteNote = "5 phút xe máy",
                streetCondition = "Đường ô tô tránh"
            ),
            RoomProperty(
                id = "p6",
                title = "Studio hiện đại Trần Thái Tông ban công kính",
                price = 2500000,
                formattedPrice = "2.500.000 đ/tháng",
                estimatedTotalCost = "~2.900.000 đ",
                rating = 4.6,
                reviewCount = 14,
                area = "20m²",
                floor = "Tầng 4/6",
                bathroomType = "Khép kín",
                balcony = "Ban công kính",
                distanceInfo = "Cách bạn 1.5km (~5 phút)",
                address = "Ngõ 68 Trần Thái Tông, Cầu Giấy, Hà Nội",
                matchPercentage = 90,
                isLandlordVerified = true,
                has360Tour = true,
                photoCount = 6,
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuATeEwiAdNjadJhptJZpSyrG6_MfCx2urQXIBew78AC7sdJ_HXyzEYpHJXWLwaRZfpUsqZTcyUMyU0O0j2VAx1ZkdZCd4vPCdUmfFcPgc8pUfcERfxuWmVxbH2HC0BNZO93J-YVM2mlLAk5iZ_52zpyxHm9HltF3GdOV8116UlLjpNq8-Av0r0jKW5FHOZZSAx9kkjmChcoWVVCiIwazsya904ibMrzDB4OZw-QmC3qxSPpr1UWpMgu",
                tourUrl = "",
                floorplanUrl = "",
                aiReasons = listOf(
                    "Ban công kính đón nắng tự nhiên",
                    "Khu vực nhiều quán ăn, cafe văn phòng",
                    "Giá thuê hợp lý chỉ 2.5 triệu"
                ),
                amenities = listOf("Ban công kính", "Điều hòa", "Nóng lạnh", "Giường tủ"),
                landlordName = "Chị Mai Linh",
                landlordAvatar = "https://lh3.googleusercontent.com/aida-public/AB6AXuCYpD_dR0LklB2coqesPODxOKPX7LxulbbFk_S-Ni8U4E9ueA2K5rpK7pvpYKBogK2e4n8A2JxNf3q2DkkaCPzhndMPeDigMogGZ4saiEL7nwsvNFZz4DqDSlVU4TlBFm9sKynbp_GSt5hlt1kaGLDCF1ebWi4mm5iTgsA_LUuZFJm7fg6O4QqzKceB2LPAaGBo-mSDfFlaZVRjN1amiS1Ss6bUXANMuVOeH_oSDC-C7mRD-33V7TGO",
                landlordExperience = "Chủ trọ 2 năm",
                landlordRating = 4.6,
                landlordReviews = 16,
                responseTime = "Dưới 10 phút",
                fixedRent = 2500000,
                electricityEstimate = 240000,
                waterEstimate = 80000,
                internetCost = 70000,
                serviceCost = 40000,
                parkingCost = 0,
                lat = 21.0310,
                lng = 105.7830,
                shortPriceLabel = "2.5tr",
                commuteNote = "5 phút xe máy",
                streetCondition = "Ngõ rộng 2.5m"
            )
        )
    }

    fun getAllProperties(): List<RoomProperty> = SAMPLE_PROPERTIES

    fun getPropertyById(id: String): RoomProperty? {
        return SAMPLE_PROPERTIES.find { it.id == id } ?: SAMPLE_PROPERTIES.firstOrNull()
    }

    fun getSavedPropertyIds(): Flow<List<String>> {
        return appDao.getAllSavedProperties().map { list -> list.map { it.id } }
    }

    suspend fun toggleSaveProperty(propertyId: String, currentSaved: Boolean) {
        if (currentSaved) {
            appDao.removeSavedProperty(propertyId)
        } else {
            appDao.saveProperty(SavedPropertyEntity(propertyId))
        }
    }

    fun getAppointments(): Flow<List<Appointment>> {
        return appDao.getAllAppointments().map { list ->
            list.map {
                Appointment(
                    id = it.id,
                    propertyId = it.propertyId,
                    propertyTitle = it.propertyTitle,
                    landlordName = it.landlordName,
                    date = it.date,
                    time = it.time,
                    contactPhone = it.contactPhone,
                    address = it.address,
                    status = it.status,
                    note = it.note
                )
            }
        }
    }

    suspend fun bookAppointment(
        propertyId: String,
        propertyTitle: String,
        landlordName: String,
        date: String,
        time: String,
        contactPhone: String,
        address: String,
        note: String
    ) {
        val entity = AppointmentEntity(
            id = UUID.randomUUID().toString(),
            propertyId = propertyId,
            propertyTitle = propertyTitle,
            landlordName = landlordName,
            date = date,
            time = time,
            contactPhone = contactPhone,
            address = address,
            status = "Đã xác nhận",
            note = note
        )
        appDao.insertAppointment(entity)
    }

    suspend fun cancelAppointment(appointmentId: String) {
        appDao.cancelAppointment(appointmentId)
    }

    fun getInitialChatMessages(): List<ChatMessage> {
        return listOf(
            ChatMessage(
                id = "m1",
                isUser = true,
                text = "Tôi là sinh viên ĐH Ngoại Thương, muốn tìm phòng dưới 3.2 triệu, cách trường tối đa 3km, phải có điều hòa, WC riêng, có chỗ để xe máy an toàn và khu vực yên tĩnh để học bài.",
                timestamp = "14:32"
            ),
            ChatMessage(
                id = "m2",
                isUser = false,
                text = "Bộ giải mã nhu cầu tự động của Trọ Việt đã kích hoạt và trích xuất thành công các tiêu chí của bạn.",
                timestamp = "14:32",
                structuredAnalysis = AiAnalysis(
                    confidenceRate = 96,
                    maxBudget = "≤ 3.200.000 đ/tháng",
                    radiusKm = "≤ 3.0 km từ ĐH Ngoại Thương",
                    requiredAmenities = listOf("WC riêng", "Điều hòa", "Chỗ để xe"),
                    softWeightings = listOf(
                        SoftWeight("Yên tĩnh học bài", 0.9f),
                        SoftWeight("An ninh xe máy", 0.85f)
                    )
                ),
                summaryText = "Mình đã quét 142 phòng quanh ĐH Ngoại Thương và lọc được 18 phòng vượt qua toàn bộ tiêu chí bắt buộc. Dưới đây là 2 gợi ý có Recommendation Score cao nhất được cá nhân hóa cho bạn:",
                recommendedProperties = listOf(
                    SAMPLE_PROPERTIES[3], // Chùa Láng
                    SAMPLE_PROPERTIES[4]  // Huỳnh Thúc Kháng
                )
            )
        )
    }
}
