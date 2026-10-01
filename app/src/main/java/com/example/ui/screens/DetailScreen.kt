package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Elevator
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.RoomProperty
import com.example.ui.components.BookingDialog
import com.example.ui.components.CommonHeader
import com.example.ui.components.CostBreakdownDialog
import com.example.ui.theme.TroVietPrimary
import com.example.ui.theme.TroVietSecondary
import com.example.ui.theme.TroVietStar
import com.example.ui.theme.TroVietTertiary
import com.example.ui.theme.TroVietTertiaryContainer
import com.example.ui.theme.TroVietTertiaryFixed
import kotlin.math.roundToInt

const val MAP_SNIPPET_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuCsGcqkUlAbn9HTBtQWS_LVB-sVbroHNoRL4wr1azucfatjiAq4islHVeKaR9sysgJXYiDeeT78eiNphjxwlSri9qmTXDdin9EvxoNP0WaH9dOZuRJobiAgE1gPlX59VoJeU15vgpw2a-l80QQTAcjhECDspl9dgHe8gZNmIA48vhgc5t6Y_eTY4ntikRtZ4hTAm888bf1Ne5qwHPY6J-0PhUBVKOeI4gFdWG0eC5mH88_TlyDJxwwQ"

@Composable
fun DetailScreen(
    property: RoomProperty,
    isSaved: Boolean,
    onBack: () -> Unit,
    onSaveToggle: () -> Unit,
    onChatClick: () -> Unit,
    onOpenMap: () -> Unit,
    onBookViewing: (date: String, time: String, phone: String, note: String) -> Unit
) {
    var mediaMode by remember { mutableStateOf("photos") } // "photos", "tour", "floorplan"
    var showBookingModal by remember { mutableStateOf(false) }
    var showCostCalculator by remember { mutableStateOf(false) }

    // Interactive 360 drag simulation
    var tourPanOffset by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            CommonHeader(
                title = "Chi Tiết Phòng Trọ",
                showBackButton = true,
                onBackClick = onBack,
                onShareClick = {}
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 90.dp)
            ) {
                // 1. Media Viewport with Tabs (Photos, 360 Tour, Floorplan)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        when (mediaMode) {
                            "photos" -> {
                                AsyncImage(
                                    model = property.imageUrl,
                                    contentDescription = property.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            "tour" -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black)
                                        .pointerInput(Unit) {
                                            detectDragGestures { change, dragAmount ->
                                                change.consume()
                                                tourPanOffset = (tourPanOffset + dragAmount.x).coerceIn(-300f, 300f)
                                            }
                                        }
                                ) {
                                    AsyncImage(
                                        model = property.tourUrl.ifEmpty { property.imageUrl },
                                        contentDescription = "360 Tour View",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .offset { IntOffset(tourPanOffset.roundToInt(), 0) }
                                    )

                                    // Tour overlay label
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(Color.Black.copy(alpha = 0.75f))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Sync, contentDescription = null, tint = TroVietTertiaryFixed, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Kéo để xoay 360° thực tế ảo", fontSize = 12.sp, color = Color.White)
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(end = 16.dp, bottom = 48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White.copy(alpha = 0.9f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(TroVietTertiary))
                                            Text("VR Ready", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TroVietTertiary)
                                        }
                                    }
                                }
                            }
                            "floorplan" -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = property.floorplanUrl.ifEmpty { property.imageUrl },
                                        contentDescription = "Sơ đồ mặt bằng 2D/3D",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }

                        // Top Left: 100% verified badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.95f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(14.dp))
                                Text("Chính chủ 100%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TroVietTertiary)
                            }
                        }

                        // Top Right: Favorite button
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.95f))
                                .clickable { onSaveToggle() }
                                .testTag("favorite_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Lưu phòng",
                                tint = if (isSaved) TroVietPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Bottom Center: Tab Switcher
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 12.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Black.copy(alpha = 0.75f))
                                .padding(4.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                MediaTabPill(
                                    title = "Hình ảnh (${property.photoCount})",
                                    isSelected = mediaMode == "photos",
                                    onClick = { mediaMode = "photos" }
                                )
                                MediaTabPill(
                                    title = "360° Tour",
                                    icon = Icons.Default.AutoAwesome,
                                    isSelected = mediaMode == "tour",
                                    onClick = { mediaMode = "tour" }
                                )
                                MediaTabPill(
                                    title = "Sơ đồ 2D/3D",
                                    isSelected = mediaMode == "floorplan",
                                    onClick = { mediaMode = "floorplan" }
                                )
                            }
                        }
                    }
                }

                // 2. Trust guarantee bar
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(TroVietTertiary.copy(alpha = 0.08f))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Đã xác minh danh tính chủ trọ & hình ảnh quay chụp thực tế",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TroVietTertiary
                        )
                    }
                }

                // 3. Price, Title, Address & Quick Specs Grid
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Price & Contract terms
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = property.formattedPrice.replace("/tháng", ""),
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TroVietPrimary
                                )
                                Text(text = " đ/tháng", fontSize = 14.sp, color = TroVietPrimary, fontWeight = FontWeight.Bold)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("Cọc 1 tháng · HĐ 6-12T", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Title
                        Text(
                            text = property.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Address
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(property.address, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 4 Specs Grid (Area, Floor, Bathroom, Balcony)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SpecBox(icon = Icons.Default.AspectRatio, title = "Diện tích", value = property.area, modifier = Modifier.weight(1f))
                            SpecBox(icon = Icons.Default.Elevator, title = "Vị trí", value = property.floor, modifier = Modifier.weight(1f))
                            SpecBox(icon = Icons.Default.Shower, title = "Phòng tắm", value = property.bathroomType, modifier = Modifier.weight(1f))
                            SpecBox(icon = Icons.Default.WbSunny, title = "Ban công", value = property.balcony, modifier = Modifier.weight(1f))
                        }
                    }
                }

                // 4. AI Assessment Card (96% Match)
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(TroVietPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Đánh giá độ phù hợp AI", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                        Text("Dựa trên hồ sơ tìm phòng của bạn", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("${property.matchPercentage}%", fontSize = 22.sp, fontWeight = FontWeight.Black, color = TroVietTertiary)
                                    Text("Rất tương thích", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TroVietTertiary)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 4 assessment rows
                            AssessmentRow(
                                icon = Icons.Default.Savings,
                                title = "Tiết kiệm ngân sách",
                                desc = "Thấp hơn 400.000 đ so với mức trần dự kiến của bạn (3.2 Tr)."
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            AssessmentRow(
                                icon = Icons.Default.NearMe,
                                title = "Vị trí & Đi lại tối ưu",
                                desc = "Chỉ 1.1 km đến Đại học Quốc Gia / Sư Phạm (khoảng 4 phút đi xe)."
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            AssessmentRow(
                                icon = Icons.Default.CheckCircle,
                                title = "Tiện nghi đầy đủ",
                                desc = "Máy lạnh Inverter, Nóng lạnh, Giường nệm, Tủ âm tường, Chỗ để xe miễn phí tầng 1."
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            AssessmentRow(
                                icon = Icons.Default.Security,
                                title = "An ninh & Giờ giấc: 9.4/10",
                                desc = "Cổng khóa vân tay thông minh 2 lớp, camera an ninh 24/7, giờ giấc tự do không chung chủ."
                            )
                        }
                    }
                }

                // 5. Cost Breakdown Table
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("Dự toán chi phí trọn gói", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(TroVietPrimary.copy(alpha = 0.12f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("AI ước tính", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TroVietPrimary)
                                        }
                                    }
                                    Text("Minh bạch không phụ phí ẩn · Sai số dự báo < 5%", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                IconButton(onClick = { showCostCalculator = true }) {
                                    Icon(Icons.Default.Calculate, contentDescription = "Tính chi phí", tint = TroVietPrimary, modifier = Modifier.size(24.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            DetailCostRow(icon = Icons.Default.Home, label = "Tiền thuê phòng cố định", amount = "2.800.000 đ")
                            DetailCostRow(icon = Icons.Default.Bolt, label = "Tiền điện sinh hoạt", sub = "3.500 đ/kWh (~80 kWh)", amount = "280.000 đ")
                            DetailCostRow(icon = Icons.Default.WaterDrop, label = "Nước sinh hoạt", sub = "30.000 đ/khối (~3 khối)", amount = "90.000 đ")
                            DetailCostRow(icon = Icons.Default.Wifi, label = "Internet Wi-Fi Mesh", amount = "80.000 đ")
                            DetailCostRow(icon = Icons.Default.CleaningServices, label = "Vệ sinh & thang máy chung", amount = "50.000 đ")
                            DetailCostRow(icon = Icons.Default.TwoWheeler, label = "Gửi xe máy tầng 1", amount = "Miễn phí (0 đ)", isFree = true)

                            Spacer(modifier = Modifier.height(10.dp))

                            // Total card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("TỔNG CHI PHÍ MỖI THÁNG", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("Đã bao gồm toàn bộ dịch vụ", fontSize = 11.sp, color = TroVietTertiary, fontWeight = FontWeight.Medium)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("3.300.000 đ", fontSize = 17.sp, fontWeight = FontWeight.Black, color = TroVietPrimary)
                                        Text("/tháng", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                // 6. Landlord Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = property.landlordAvatar,
                                        contentDescription = property.landlordName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(property.landlordName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                            Icon(Icons.Default.Verified, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(15.dp))
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Star, contentDescription = null, tint = TroVietStar, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text("${property.landlordRating}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                            Text(" (${property.landlordReviews} đánh giá xác thực)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(property.landlordExperience, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TroVietTertiary)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Guarantee note
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TroVietTertiary.copy(alpha = 0.08f))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.Policy, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Cam kết Trọ Việt Bảo Chứng: Không phát sinh bất kỳ chi phí ngoài hợp đồng. Đền cọc 100% nếu phòng không đúng thực tế mô tả hoặc phát sinh tranh chấp.",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Phản hồi tin nhắn: ${property.responseTime}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { /* Landlord profile */ }
                                ) {
                                    Text("Xem hồ sơ chủ nhà", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TroVietPrimary)
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                // 7. Location & Neighborhood Map Snippet
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Vị trí & Tiện ích lân cận", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Bán kính 1.5 km", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TroVietTertiary)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onOpenMap() }
                        ) {
                            AsyncImage(
                                model = MAP_SNIPPET_URL,
                                contentDescription = "Bản đồ vị trí",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Bottom bar
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.95f))
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Cách ĐH Quốc Gia 1.1km · 4 phút", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    Text("Mở bản đồ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TroVietPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sticky Bottom CTA Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Tổng dự toán", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("3.30 Tr", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TroVietPrimary)
                        Text("/tháng", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Chat button
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onChatClick() }
                            .testTag("chat_landlord_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "Nhắn tin", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(22.dp))
                    }

                    // Book appointment button
                    Button(
                        onClick = { showBookingModal = true },
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("btn_book_room"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TroVietPrimary)
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Hẹn xem phòng", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showBookingModal) {
        BookingDialog(
            property = property,
            onDismiss = { showBookingModal = false },
            onConfirm = { date, time, phone, note ->
                onBookViewing(date, time, phone, note)
                showBookingModal = false
            }
        )
    }

    if (showCostCalculator) {
        CostBreakdownDialog(
            baseRent = property.fixedRent,
            onDismiss = { showCostCalculator = false }
        )
    }
}

@Composable
private fun MediaTabPill(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) TroVietPrimary else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = if (isSelected) Color.White else Color.LightGray, modifier = Modifier.size(13.dp))
            }
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color.LightGray
            )
        }
    }
}

@Composable
private fun SpecBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun AssessmentRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(2.dp))
            Text(desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
        }
    }
}

@Composable
private fun DetailCostRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    sub: String? = null,
    amount: String,
    isFree: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isFree) TroVietTertiary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(17.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                if (sub != null) {
                    Text(sub, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Text(
            text = amount,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isFree) TroVietTertiary else MaterialTheme.colorScheme.onSurface
        )
    }
}
