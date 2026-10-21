package com.example.currencyconverter

import android.graphics.Paint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // View Binding : accès direct aux vues
    private lateinit var binding: ActivityMainBinding

    // Taux de change fixes, exprimés pour 1 EUR
    private val taux = mapOf(
        "EUR" to 1.0, "USD" to 1.17, "TND" to 3.40,
        "GBP" to 0.87, "JPY" to 172.50, "CAD" to 1.62
    )

    // Longueur maximale du montant saisi
    private val longueurMaxMontant = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Souligne le montant et le résultat
        binding.tvMontant.paintFlags = binding.tvMontant.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tvResultat.paintFlags = binding.tvResultat.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        configurerSpinners()
        configurerClavier()

        binding.btnConvertir.setOnClickListener { lancerConversion() }
        binding.btnInverser.setOnClickListener { inverserDevises() }
    }

    /** Remplit les deux Spinner depuis R.array.devises (défaut : USD -> EUR). */
    private fun configurerSpinners() {
        val devises = resources.getStringArray(R.array.devises)
        val adapter = ArrayAdapter(this, R.layout.item_spinner, devises)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spDeviseSource.adapter = adapter
        binding.spDeviseCible.adapter = adapter

        binding.spDeviseSource.setSelection(devises.indexOf("USD"))
        binding.spDeviseCible.setSelection(devises.indexOf("EUR"))
    }

    /** Associe chaque touche du clavier à son action. */
    private fun configurerClavier() {
        val touchesCaracteres = mapOf(
            binding.btn0 to "0", binding.btn1 to "1", binding.btn2 to "2",
            binding.btn3 to "3", binding.btn4 to "4", binding.btn5 to "5",
            binding.btn6 to "6", binding.btn7 to "7", binding.btn8 to "8",
            binding.btn9 to "9", binding.btnPoint to "."
        )
        for ((bouton, caractere) in touchesCaracteres) {
            bouton.setOnClickListener { ajouterCaractere(caractere) }
        }
        binding.btnEffacer.setOnClickListener { effacer() }
    }

    /** Ajoute un chiffre ou un point à la fin du montant. */
    private fun ajouterCaractere(c: String) {
        val montantActuel = binding.tvMontant.text.toString()

        // Un seul point décimal autorisé
        if (c == "." && montantActuel.contains(".")) return

        // Le "0" initial est remplacé par le premier chiffre tapé
        val nouveauMontant = if (montantActuel == "0" && c != ".") c else montantActuel + c

        // Limite de 10 caractères
        if (nouveauMontant.length > longueurMaxMontant) return

        binding.tvMontant.text = nouveauMontant
    }

    /** Touche C : montant à 0 et résultat vidé. */
    private fun effacer() {
        binding.tvMontant.text = getString(R.string.montant_initial)
        binding.tvResultat.text = ""
    }

    /** Vérifie le montant puis affiche le résultat. */
    private fun lancerConversion() {
        val montant = binding.tvMontant.text.toString().toDoubleOrNull() ?: 0.0
        if (montant == 0.0) {
            Toast.makeText(this, R.string.msg_montant_vide, Toast.LENGTH_SHORT).show()
            return
        }
        val source = binding.spDeviseSource.selectedItem.toString()
        val cible = binding.spDeviseCible.selectedItem.toString()

        val resultat = convertir(montant, source, cible)
        // Locale.US : toujours un point comme séparateur décimal
        binding.tvResultat.text = String.format(Locale.US, "%.2f", resultat)
    }

    /** Conversion avec l'euro comme devise pivot. */
    private fun convertir(montant: Double, source: String, cible: String): Double {
        val montantEnEuro = montant / taux.getValue(source)
        return montantEnEuro * taux.getValue(cible)
    }

    /** Échange les devises, puis relance la conversion si un résultat est affiché. */
    private fun inverserDevises() {
        val positionSource = binding.spDeviseSource.selectedItemPosition
        val positionCible = binding.spDeviseCible.selectedItemPosition
        binding.spDeviseSource.setSelection(positionCible)
        binding.spDeviseCible.setSelection(positionSource)

        if (binding.tvResultat.text.isNotEmpty()) {
            lancerConversion()
        }
    }
}