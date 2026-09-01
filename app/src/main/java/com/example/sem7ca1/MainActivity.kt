package com.example.sem7ca1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sem7ca1.ui.theme.Sem7CA1Theme
import kotlinx.coroutines.delay
import java.util.Locale

data class Stock(val symbol: String, val price: Double, val change: Double, val volume: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Sem7CA1Theme { StockMarketApp() } }
    }
}

@Composable
fun StockMarketApp() {
    var loading by remember { mutableStateOf(true) }
    var ticks by remember { mutableIntStateOf(0) }
    var stock by remember { mutableStateOf(Stock("AAPL", 185.92, 1.25, "52.4M")) }

    LaunchedEffect(Unit) {
        delay(2000)
        loading = false
        while (true) {
            delay(3000)
            stock = stock.copy(
                price = (stock.price + if (ticks % 2 == 0) 0.8 else -0.5),
                change = if (ticks % 2 == 0) 1.25 else -0.75
            )
            ticks++
        }
    }
    SideEffect { Log.d("StockApp", "SideEffect: UI updated (Ticks = $ticks)") }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Stock Market Application", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Live Ticks: $ticks", fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("Loading prices...")
            } else {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(stock.symbol, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Volume: ${stock.volume}", color = Color.Gray)
                        Spacer(Modifier.height(8.dp))
                        Text("Price: $${String.format(Locale.US, "%.2f", stock.price)}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "Change: ${if (stock.change >= 0) "+" else ""}${stock.change}%",
                            color = if (stock.change >= 0) Color(0xFF2E7D32) else Color(0xFFC62828),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
