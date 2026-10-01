package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.RoomProperty
import com.example.ui.components.BookingDialog
import com.example.ui.components.CostBreakdownDialog
import com.example.ui.theme.TroVietPrimary
import com.example.ui.theme.TroVietSecondary
import com.example.ui.theme.TroVietStar
import com.example.ui.theme.TroVietTertiary
import com.example.ui.theme.TroVietTertiaryContainer
import com.example.ui.theme.TroVietTertiaryFixed

const val MAP_BG_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuAEykXHMZO0J_9Ku-WELh3AAt989cEDcYRW6SI5erf97By1iviJlw-IAomFYnGajohOX-WgZSyPXZO_DNGGTM0OjykE0X1IOpF7um_ttpWedVw3h266CtEtIyt3kLKwbeLc6Dm6CKtIrf5twOTk9qdfKRMO-krKg7aashqN04kbrLhKnilqWm7z24qE8ajLkM637p1XceP6nJ_Y5dTAK87lqDvO9wLFQu02laaguHIdKRXsLSZ7Onrs"

@Composable
fun MapScreen(
    properties: List<RoomProperty>,
    savedPropertyIds: List<String>,
    onPropertyClick: (RoomProperty) -> Unit,
    onSaveToggle: (RoomProperty) -> Unit,
    onBookViewing: (RoomProperty, String, String, String, String) -> Unit
) {
    var radiusKm by remember { mutableStateOf("3.0 km") }
    var selectedPropertyIndex by remember { mutableIntStateOf(0) }
    val currentProperty = properties.getOrElse(selectedPropertyIndex) { properties.first() }

    var showPoiLayers by remember { mutableStateOf(true) }
    var bookingPropertyTarget by remember { mutableStateOf<RoomProperty?>(null) }
    var showCostCalculator by remember { mutableStateOf(false) }

    // Pulsing radar animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Map Container
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            val mapHeight = maxHeight

            // Map Background Texture
            AsyncImage(
                model = MAP_BG_URL,
                contentDescription = "Bản đồ vệ tinh",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Canvas vector roads and metro lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width * 0.48f
                val cy = size.height * 0.45f

                // Pulse ring for GPS
                drawCircle(
                    color = Color(0xFFDB322F).copy(alpha = 0.08f),
                    radius = 160f * pulseScale,
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = Color(0xFFDB322F).copy(alpha = 0.35f),
                    radius = 160f,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                )

                // Outer radius ring
                drawCircle(
                    color = Color(0xFF006947).copy(alpha = 0.25f),
                    radius = 280f,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f))
                )
            }

            // POI Markers
            if (showPoiLayers) {
                // Metro Cầu Giấy
                Box(
                    modifier = Modifier
                        .offset(x = 65.dp, y = 190.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TroVietTertiary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(Icons.Default.Train, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                        Text("Metro Cầu Giấy", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // ĐH Quốc Gia
                Box(
                    modifier = Modifier
                        .offset(x = 18.dp, y = 145.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(Icons.Default.School, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(12.dp))
                        Text("ĐH Quốc Gia", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    }
                }

                // Chợ Nhà Xanh
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-60).dp, y = 148.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(Icons.Default.LocalMall, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(12.dp))
                        Text("Chợ Nhà Xanh", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Street names
            Text(
                text = "Đ. XUÂN THỦY",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                modifier = Modifier
                    .offset(x = 24.dp, y = 175.dp)
                    .rotate(-4f)
            )

            Text(
                text = "NGUYỄN PHONG SẮC",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-30).dp, y = 60.dp)
                    .rotate(12f)
            )

            // USER LOCATION PIN (Center)
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (-10).dp, y = (-40).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(TroVietPrimary.copy(alpha = 0.25f))
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(3.dp, TroVietPrimary, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(text = "Vị trí của bạn", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // CANDIDATE PROPERTY PINS
            // Pin 1: 3.2tr (Dịch Vọng Hậu)
            MapPropertyPin(
                priceLabel = "3.2tr",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-40).dp, y = 205.dp),
                isSelected = selectedPropertyIndex == 1,
                onClick = { selectedPropertyIndex = 1 }
            )

            // Pin 2: 2.5tr (Trần Thái Tông)
            MapPropertyPin(
                priceLabel = "2.5tr",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = 45.dp, y = 40.dp),
                isSelected = selectedPropertyIndex == 5,
                onClick = { selectedPropertyIndex = 5 }
            )

            // Pin 3: 3.0tr 91% (Chùa Láng)
            MapPropertyPin(
                priceLabel = "3.0tr",
                badge = "91%",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-60).dp, y = (-260).dp),
                isSelected = selectedPropertyIndex == 3,
                onClick = { selectedPropertyIndex = 3 }
            )

            // Pin 4: 2.1tr (Phan Văn Trường)
            MapPropertyPin(
                priceLabel = "2.1tr",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = (-30).dp, y = 135.dp),
                isSelected = selectedPropertyIndex == 2,
                onClick = { selectedPropertyIndex = 2 }
            )

            // Pin 0 (SELECTED): 2.8tr - 96% AI (Ngõ 165 Cầu Giấy)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 40.dp, y = (-85).dp)
                    .clickable { selectedPropertyIndex = 0 },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(20.dp))
                            .background(TroVietPrimary)
                            .border(2.dp, Color.White, RoundedCornerShape(20.dp))
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                            Text("2.8tr", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("96% AI", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = TroVietPrimary)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .offset(y = (-3).dp)
                            .rotate(45f)
                            .background(TroVietPrimary)
                    )
                }
            }

            // Right Floating Control Panel
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 115.dp, end = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // My Location
                FloatingMapIconBtn(icon = Icons.Default.MyLocation, isPrimary = true) {
                    // Recenter
                }
                // POI toggle
                FloatingMapIconBtn(icon = Icons.Default.Layers, hasDot = showPoiLayers) {
                    showPoiLayers = !showPoiLayers
                }
                // Noise & Safety
                FloatingMapIconBtn(icon = Icons.Default.Security) {
                    // Toggle
                }
                // List view
                FloatingMapIconBtn(icon = Icons.Default.FormatListBulleted) {
                    // Toggle
                }
            }

            // Top Floating Search Bar & Pills
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Location & Radius search pill
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TroVietPrimary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.NearMe, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text("Quận Cầu Giấy", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(TroVietTertiary))
                                    Text("AI Đang quét", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TroVietTertiary)
                                }
                                Text("Bán kính quét: $radiusKm • ${properties.size} phòng phù hợp", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        // Radius pill toggle
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    radiusKm = when (radiusKm) {
                                        "1.5 km" -> "3.0 km"
                                        "3.0 km" -> "5.0 km"
                                        else -> "1.5 km"
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurface)
                                Text(radiusKm, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick horizontal filter pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterPill(text = "≤ 3.5tr", icon = Icons.Default.Payments, isSelected = true)
                    FilterPill(text = "4.5+", icon = Icons.Default.Star, isSelected = false, iconTint = TroVietStar)
                    FilterPill(text = "WC riêng", icon = Icons.Default.Bathtub, isSelected = false)
                    FilterPill(text = "360° Tour", icon = Icons.Default.ViewInAr, isSelected = false, iconTint = TroVietTertiary)
                    FilterPill(text = "Xác minh ✓", icon = Icons.Default.Verified, isSelected = false, iconTint = TroVietTertiary)
                }
            }

            // Bottom Recommendation Drawer Overlay
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .testTag("map_bottom_drawer")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header handle and count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(TroVietTertiary))
                            Text(
                                text = "1 trong 24 phòng trong bán kính quét",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Drag indicator
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                selectedPropertyIndex = (selectedPropertyIndex + 1) % properties.size
                            }
                        ) {
                            Text(
                                text = "${selectedPropertyIndex + 1}/${properties.size}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TroVietPrimary
                            )
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Thumbnail + Details Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPropertyClick(currentProperty) }
                    ) {
                        // Thumbnail with 360 badge
                        Box(
                            modifier = Modifier
                                .size(width = 100.dp, height = 80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            AsyncImage(
                                model = currentProperty.imageUrl,
                                contentDescription = currentProperty.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            if (currentProperty.has360Tour) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Black.copy(alpha = 0.7f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("360°", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TroVietTertiaryFixed)
                                }
                            }

                            // Favorite heart icon
                            val isSaved = savedPropertyIds.contains(currentProperty.id)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(4.dp)
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.85f))
                                    .clickable { onSaveToggle(currentProperty) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isSaved) TroVietPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Details
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(TroVietTertiaryContainer)
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text("CHÍNH CHỦ", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = TroVietStar, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("${currentProperty.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text(" (${currentProperty.reviewCount})", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = currentProperty.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = currentProperty.address,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = currentProperty.formattedPrice.replace("/tháng", ""),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TroVietPrimary
                                )
                                Text(" đ/tháng", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Commute Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(currentProperty.commuteNote, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text(currentProperty.distanceInfo, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TroVietTertiaryFixed.copy(alpha = 0.5f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(currentProperty.streetCondition, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TroVietTertiary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // AI Match Breakdown Matrix
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            .padding(8.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(15.dp))
                                    Text("Độ tương thích AI: ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                    Text("${currentProperty.matchPercentage}%", fontSize = 13.sp, color = TroVietPrimary, fontWeight = FontWeight.Black)
                                }
                                Text("Tối ưu theo tiêu chí cá nhân", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf("Tài chính", "Gần trường", "Tiện ích", "Yên tĩnh").forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(11.dp))
                                            Text(item, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Estimated Living Cost
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showCostCalculator = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Calculate, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tổng sinh hoạt phí ước tính:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(currentProperty.estimatedTotalCost, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // CTA Buttons Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 3D Tour
                        Box(
                            modifier = Modifier
                                .weight(2.5f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { onPropertyClick(currentProperty) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.ViewInAr, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(16.dp))
                                Text("3D Tour", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        // Chỉ đường
                        Box(
                            modifier = Modifier
                                .weight(2.5f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { /* Navigate */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Directions, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(16.dp))
                                Text("Chỉ đường", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        // Đặt lịch xem
                        Button(
                            onClick = { bookingPropertyTarget = currentProperty },
                            modifier = Modifier
                                .weight(5f)
                                .height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TroVietPrimary)
                        ) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Đặt lịch xem", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    bookingPropertyTarget?.let { prop ->
        BookingDialog(
            property = prop,
            onDismiss = { bookingPropertyTarget = null },
            onConfirm = { date, time, phone, note ->
                onBookViewing(prop, date, time, phone, note)
                bookingPropertyTarget = null
            }
        )
    }

    if (showCostCalculator) {
        CostBreakdownDialog(
            baseRent = currentProperty.fixedRent,
            onDismiss = { showCostCalculator = false }
        )
    }
}

@Composable
private fun MapPropertyPin(
    priceLabel: String,
    badge: String? = null,
    modifier: Modifier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .shadow(4.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) TroVietPrimary else Color.White)
                .padding(horizontal = 7.dp, vertical = 3.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                if (badge != null) {
                    Text(badge, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TroVietTertiary)
                }
                Text(
                    text = priceLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Box(
            modifier = Modifier
                .size(6.dp)
                .offset(y = (-2).dp)
                .rotate(45f)
                .background(if (isSelected) TroVietPrimary else Color.White)
        )
    }
}

@Composable
private fun FloatingMapIconBtn(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPrimary: Boolean = false,
    hasDot: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isPrimary) TroVietPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        if (hasDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(TroVietTertiary)
            )
        }
    }
}

@Composable
private fun FilterPill(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    iconTint: Color? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) TroVietPrimary else Color.White.copy(alpha = 0.95f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else iconTint ?: MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
