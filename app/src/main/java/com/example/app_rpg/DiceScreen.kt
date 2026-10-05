package com.example.app_rpg

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_rpg.ui.theme.LocalModePalette
import com.example.app_rpg.ui.theme.corBrancoOffWhite
import com.example.app_rpg.ui.theme.corBrancoPuro
import com.example.app_rpg.ui.theme.corCinzaEscuro
import com.example.app_rpg.ui.theme.corCinzaMedio
import com.example.app_rpg.ui.theme.corPretoPuro


private data class DiceStats(
    val expression: DiceExpression,
    val outcomes: List<Outcome>
) {
    val median: Int = outcomes.median()
    val bars: List<Bar> = outcomes.map { outcome ->
        Bar(
            label = outcome.total.toString(),
            value = (outcome.probability * 100).toFloat(),
            highlighted = outcome.total == median
        )
    }
}

@Composable
fun DiceScreen(modifier: Modifier = Modifier) {
    val expressionState = rememberTextFieldState("2d6 + 3")

    var stats by remember { mutableStateOf<DiceStats?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    fun submit() {
        parseDiceExpression(expressionState.text.toString())
            .onSuccess { expression ->
                stats = DiceStats(expression, expression.distribution())
                error = null
            }
            .onFailure { failure ->
                stats = null
                error = failure.message
            }
    }

    Column(
        modifier = modifier
            .background(corPretoPuro)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExpressionSection(
            state = expressionState,
            error = error,
            onSubmit = ::submit
        )

        val currentStats = stats
        DataSection(currentStats)

        if (currentStats != null) {
            DistributionSection(currentStats.bars)
        } else {
            Text(
                text = "Digite uma expressão como 2d6 + 3 para ver a distribuição.",
                color = corCinzaMedio,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ExpressionSection(
    state: TextFieldState,
    error: String?,
    onSubmit: () -> Unit
) {
    val palette = LocalModePalette.current
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            state = state,
            label = { Text("Expressão") },
            placeholder = { Text("2d6 + 3") },
            lineLimits = TextFieldLineLimits.SingleLine,
            isError = error != null,
            supportingText = error?.let { message -> { Text(message) } },
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = corCinzaEscuro,
                unfocusedContainerColor = corCinzaEscuro,
                errorContainerColor = corCinzaEscuro,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = corBrancoOffWhite,
                unfocusedTextColor = corBrancoOffWhite,
                cursorColor = palette.accent,
                focusedLabelColor = palette.accent,
                unfocusedLabelColor = corCinzaMedio,
                focusedPlaceholderColor = corCinzaMedio,
                unfocusedPlaceholderColor = corCinzaMedio,
                errorLabelColor = palette.soft,
                errorSupportingTextColor = palette.soft
            )
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.accent,
                contentColor = corBrancoPuro
            )
        ) {
            Text(
                text = "=",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DataSection(stats: DiceStats?) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ResultCard(
                valor = stats?.let { "%.2f".format(it.expression.mean) },
                label = "Média",
                modifier = Modifier.weight(1f)
            )
            ResultCard(
                valor = stats?.median?.toString(),
                label = "Mediana",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ResultCard(
                valor = stats?.expression?.min?.toString(),
                label = "Mín",
                modifier = Modifier.weight(1f)
            )
            ResultCard(
                valor = stats?.expression?.max?.toString(),
                label = "Máx",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DistributionSection(bars: List<Bar>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = corCinzaEscuro),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Distribuição (%)",
                color = corCinzaMedio,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            BarChart(data = bars, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ResultCard(
    valor: String?,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.aspectRatio(2f),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = corCinzaEscuro),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = valor ?: "—",
                color = if (valor == null) corCinzaMedio else corBrancoPuro,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 30.sp,
                maxLines = 1
            )
            Text(
                text = label,
                color = corCinzaMedio,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 13.sp,
                maxLines = 1
            )
        }
    }
}

data class Bar(
    val label: String,
    val value: Float,
    val highlighted: Boolean = false
)

@Composable
fun BarChart(
    data: List<Bar>,
    modifier: Modifier = Modifier,
    chartHeight: Dp = 120.dp,
    barColor: Color = LocalModePalette.current.accent,
    highlightColor: Color = LocalModePalette.current.soft,
    formatValue: (Float) -> String = { "%.0f".format(it) }
) {
    if (data.isEmpty()) return

    val peak = data.maxOfOrNull { it.value }?.takeIf { it > 0f } ?: 1f
    val showValues = data.size <= 9

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        data.forEach { bar ->
            key(bar.label) {
                val fraction by animateFloatAsState(
                    targetValue = bar.value / peak,
                    label = "barHeight"
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showValues) {
                        Text(
                            text = formatValue(bar.value),
                            color = corCinzaMedio,
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(chartHeight * fraction)
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(if (bar.highlighted) highlightColor else barColor)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = bar.label,
                        color = if (bar.highlighted) corBrancoPuro else corCinzaMedio,
                        fontSize = 10.sp,
                        fontWeight = if (bar.highlighted) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

data class DiceExpression(
    val count: Int,
    val sides: Int,
    val modifier: Int = 0
) {
    val min: Int get() = count + modifier
    val max: Int get() = count * sides + modifier
    val mean: Double get() = count * (sides + 1) / 2.0 + modifier

    override fun toString(): String = buildString {
        append(count).append('d').append(sides)
        when {
            modifier > 0 -> append(" + ").append(modifier)
            modifier < 0 -> append(" - ").append(-modifier)
        }
    }
}

data class Outcome(val total: Int, val probability: Double)

private val DICE_REGEX = Regex("""^\s*(\d+)?\s*[dD]\s*(\d+)\s*(?:([+-])\s*(\d+)\s*)?$""")

private const val MAX_DICE = 100
private const val MAX_SIDES = 1000

fun parseDiceExpression(input: String): Result<DiceExpression> {
    val match = DICE_REGEX.matchEntire(input)
        ?: return fail("Expressão inválida. Use algo como \"2d6 + 3\".")

    val (countText, sidesText, sign, constantText) = match.destructured

    val count = countText.ifEmpty { "1" }.toIntOrNull()
        ?: return fail("Quantidade de dados grande demais.")

    val sides = sidesText.toIntOrNull()
        ?: return fail("Número de faces grande demais.")

    if (count !in 1..MAX_DICE) return fail("A quantidade de dados deve estar entre 1 e $MAX_DICE.")
    if (sides !in 2..MAX_SIDES) return fail("O dado deve ter entre 2 e $MAX_SIDES faces.")

    val modifier = if (constantText.isEmpty()) 0 else {
        val magnitude = constantText.toIntOrNull() ?: return fail("Modificador grande demais.")
        if (sign == "-") -magnitude else magnitude
    }

    return Result.success(DiceExpression(count, sides, modifier))
}

private fun fail(message: String): Result<DiceExpression> =
    Result.failure(IllegalArgumentException(message))

fun DiceExpression.distribution(): List<Outcome> {
    val p = 1.0 / sides
    var probs = doubleArrayOf(1.0)

    repeat(count) {
        val next = DoubleArray(probs.size + sides)
        for (i in probs.indices) {
            val carried = probs[i]
            if (carried == 0.0) continue
            for (face in 1..sides) {
                next[i + face] += carried * p
            }
        }
        probs = next
    }

    return (count..count * sides).map { raw ->
        Outcome(total = raw + modifier, probability = probs[raw])
    }
}

private const val EPSILON = 1e-9

fun List<Outcome>.median(): Int {
    require(isNotEmpty()) { "Cannot take the median of an empty distribution." }

    var cumulative = 0.0
    for (outcome in this) {
        cumulative += outcome.probability
        if (cumulative >= 0.5 - EPSILON) return outcome.total
    }
    return last().total
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DiceScreenPreview() {
    DiceScreen(Modifier.fillMaxSize())
}
