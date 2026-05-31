package com.nabeelkm.workout.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nabeelkm.workout.theme.ThemeColor


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(18.dp, 10.dp),
    content: @Composable RowScope.() -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = contentPadding,
        shape = RoundedCornerShape(10.dp),
        content = content
    )
}