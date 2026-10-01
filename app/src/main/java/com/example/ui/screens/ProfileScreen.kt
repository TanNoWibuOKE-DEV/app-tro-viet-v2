package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.AVATAR_URL
import com.example.ui.components.CommonHeader
import com.example.ui.theme.TroVietPrimary
import com.example.ui.theme.TroVietTertiary
import com.example.ui.theme.TroVietTertiaryContainer
import com.example.ui.theme.TroVietTertiaryFixed
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    onExploreRooms: () -> Unit
) {
    var maxBudget by remember { mutableFloatStateOf(3200000f) }
    var maxDistance by remember { mutableFloatStateOf(3.0f) }
    var alertNewRooms by remember { mutableStateOf(true) }
    var requireAirConditioning by remember { mutableStateOf(true) }
    var requirePrivateBathroom by remember { mutableStateOf(true) }

    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_screen")
    ) {
        CommonHeader(
            title = "Hồ Sơ & Cấu Hình AI",
            subtitle = "Minh Trang • FTU"
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .border(2.dp, TroVietPrimary, CircleShape)
                    ) {
                        AsyncImage(
                            model = AVATAR_URL,
                            contentDescription = "Avatar Minh Trang",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Nguyễn Minh Trang",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(Icons.Default.Verified, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(16.dp))
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.School, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            Text(
                                text = "Sinh viên ĐH Ngoại Thương (FTU)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(TroVietTertiaryFixed)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Đã xác thực thẻ sinh viên ✓",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TroVietTertiary
                            )
                        }
                    }
                }
            }

            // AI Preference Engine Parameters
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TroVietPrimary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, tint = TroVietPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Trọng Số Tìm Kiếm AI", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Tự động áp dụng cho gợi ý Trang chủ", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Budget Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Ngân sách tối đa trọn gói", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            text = "${formatter.format(maxBudget.toLong())} đ",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TroVietPrimary
                        )
                    }
                    Slider(
                        value = maxBudget,
                        onValueChange = { maxBudget = it },
                        valueRange = 1800000f..5000000f,
                        steps = 31,
                        colors = SliderDefaults.colors(thumbColor = TroVietPrimary, activeTrackColor = TroVietPrimary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Distance Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Bán kính tối đa từ trường", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            text = String.format(Locale.US, "%.1f km", maxDistance),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TroVietTertiary
                        )
                    }
                    Slider(
                        value = maxDistance,
                        onValueChange = { maxDistance = it },
                        valueRange = 0.5f..7.0f,
                        colors = SliderDefaults.colors(thumbColor = TroVietTertiary, activeTrackColor = TroVietTertiary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Toggles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Bắt buộc điều hòa Inverter", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                        Switch(
                            checked = requireAirConditioning,
                            onCheckedChange = { requireAirConditioning = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TroVietPrimary, checkedTrackColor = TroVietPrimary.copy(alpha = 0.5f))
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("WC riêng khép kín", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                        Switch(
                            checked = requirePrivateBathroom,
                            onCheckedChange = { requirePrivateBathroom = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TroVietPrimary, checkedTrackColor = TroVietPrimary.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            // Notification & Safety
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Thông báo phòng mới phù hợp", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Nhận cảnh báo khi có phòng vừa đăng khớp 90%+", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = alertNewRooms,
                            onCheckedChange = { alertNewRooms = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = TroVietTertiary, checkedTrackColor = TroVietTertiary.copy(alpha = 0.5f))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Privacy policy */ }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = TroVietTertiary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Chính sách Bảo chứng cọc an toàn Trọ Việt", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onExploreRooms,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TroVietPrimary)
            ) {
                Text("Áp dụng và quay lại tìm phòng", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}
