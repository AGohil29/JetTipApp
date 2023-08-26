package com.example.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.utils.calculateTotalPerPerson
import com.example.jettipapp.utils.calculateTotalTip
import com.example.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Text(text = "Hello Again")
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }

}

@Composable
fun TopHeader(totalPerPerson: Double = 134.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(16.dp))),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {
    val numberOfSplit = remember {
        mutableStateOf(1)
    }
    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    val splitRange = IntRange(1, 100)

    BillForm(
        numberOfSplit = numberOfSplit,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState,
        splitRange = splitRange
    ) {
        Log.d("Arun", it)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    numberOfSplit: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    splitRange: IntRange = 1..100,
    onValChange: (String) -> Unit
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    Column {
        TopHeader(totalPerPerson = totalPerPersonState.value)

        Surface(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Column() {
                InputField(valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
                        keyboardController?.hide()
                    }
                )
                if (validState) {
                    Row(
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.width(120.dp))
                        Row(
                            modifier = Modifier.padding(3.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                                if (numberOfSplit.value > splitRange.first) numberOfSplit.value -= 1
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    tipPercentage,
                                    numberOfSplit.value
                                )
                            })

                            Text(
                                text = numberOfSplit.value.toString(),
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp)
                            )

                            RoundIconButton(
                                imageVector = Icons.Default.Add,
                                onClick = {
                                    if (numberOfSplit.value < splitRange.last) numberOfSplit.value += 1
                                    totalPerPersonState.value = calculateTotalPerPerson(
                                        totalBillState.value.toDouble(),
                                        tipPercentage,
                                        numberOfSplit.value
                                    )
                                })
                        }
                    }

                    //Tip row
                    Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                        Text(
                            text = "Tip",
                            modifier = modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.width(200.dp))
                        Text(
                            text = "$${tipAmountState.value}",
                            modifier = modifier.align(alignment = Alignment.CenterVertically)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$tipPercentage%")
                        Spacer(modifier = Modifier.height(12.dp))
                        Slider(
                            value = sliderPositionState.value, onValueChange = {
                                sliderPositionState.value = it
                                tipAmountState.value = calculateTotalTip(
                                    totalBillState.value.toDouble(),
                                    tipPercentage
                                )
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBillState.value.toDouble(),
                                    tipPercentage,
                                    numberOfSplit.value
                                )
                            },
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            steps = 5
                        )
                    }
                } else {
                    Box() {}
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        Text(text = "Hello Again")
    }
}