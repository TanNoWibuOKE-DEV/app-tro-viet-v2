package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.TroVietPrimary
import com.example.ui.theme.TroVietTertiary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CostBreakdownDialog(
    baseRent: Long = 2800000,
    onDismiss: () -> Unit
) {
    var kwh by remember { mutableFloatStateOf(80f) }
    var waterBlocks by remember { mutableFloatStateOf(3f) }

    val electricityRate = 3500L
    val waterRate = 30000L
    val internetRate = 80000L
    val serviceRate = 50000L
    val parkingRate = 0L

    val electricityTotal = (kwh * electricityRate).toLong()
    val waterTotal = (waterBlocks * waterRate).toLong()
    val totalCost = baseRent + electricityTotal + waterTotal + internetRate + serviceRate + parkingRate

    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TroVietPrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = TroVietPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Công thức tính chi phí AI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Minh bạch không phụ phí ẩn",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Breakdown items
                CostRow(
                    icon = Icons.Default.Home,
                    label = "Tiền thuê cố định",
                    sub = "Theo hợp đồng",
                    amount = "${formatter.format(baseRent)} đ"
                )

                CostRow(
                    icon = Icons.Default.Bolt,
                    label = "Tiền điện (${kwh.toInt()} kWh)",
                    sub = "3.500 đ/kWh",
                    amount = "${formatter.format(electricityTotal)} đ"
                )
                Slider(
                    value = kwh,
                    onValueChange = { kwh = it },
                    valueRange = 20f..250f,
                    colors = SliderDefaults.colors(thumbColor = TroVietPrimary, activeTrackColor = TroVietPrimary),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                CostRow(
                    icon = Icons.Default.WaterDrop,
                    label = "Tiền nước (${waterBlocks.toInt()} khối)",
                    sub = "30.000 đ/khối",
                    amount = "${formatter.format(waterTotal)} đ"
                )
                Slider(
                    value = waterBlocks,
                    onValueChange = { waterBlocks = it },
                    valueRange = 1f..10f,
                    colors = SliderDefaults.colors(thumbColor = TroVietTertiary, activeTrackColor = TroVietTertiary),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                CostRow(
                    icon = Icons.Default.Wifi,
                    label = "Internet Mesh tốc độ cao",
                    sub = "Chia đều phòng",
                    amount = "${formatter.format(internetRate)} đ"
                )

                CostRow(
                    icon = Icons.Default.CleaningServices,
                    label = "Vệ sinh & Thang máy",
                    sub = "Hàng tháng",
                    amount = "${formatter.format(serviceRate)} đ"
                )

                CostRow(
                    icon = Icons.Default.TwoWheeler,
                    label = "Gửi xe máy tầng 1",
                    sub = "Có camera & khóa vân tay",
                    amount = "Miễn phí (0 đ)",
                    isFree = true
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = DividerDefaults.color)

                // Total display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TroVietPrimary.copy(alpha = 0.08f))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TỔNG DỰ TOÁN / THÁNG",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Đã bao gồm toàn bộ dịch vụ",
                                fontSize = 12.sp,
                                color = TroVietTertiary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "${formatter.format(totalCost)} đ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TroVietPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CostRow(
    icon: ImageVector,
    label: String,
    sub: String,
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isFree) TroVietTertiary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = sub,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
