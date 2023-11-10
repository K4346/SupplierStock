package com.example.supplierstock.domain.use_cases

import android.content.Context
import android.content.Intent
import com.example.supplierstock.R
import com.example.supplierstock.data.entities.ProductEntity

class ShareUseCase {
    fun makeIntent(product: ProductEntity, context: Context): Intent {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, makeShareText(product, context))
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, null)
    }

    private fun makeShareText(product: ProductEntity, context: Context) =
        "${context.getString(R.string.product_name)}: ${product.name}\n" +
                "${context.getString(R.string.price)}: ${product.price}\n" +
                "${context.getString(R.string.quantity)}: ${product.quantity}\n" +
                "${context.getString(R.string.supplier)}: ${product.supplier}\n" +
                "${context.getString(R.string.email)}: ${product.email}\n" +
                "${context.getString(R.string.phone)}: ${product.phone}\n"
}