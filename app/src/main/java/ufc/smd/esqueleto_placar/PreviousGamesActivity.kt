package ufc.smd.esqueleto_placar

import adapters.CustomAdapter
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import kotlin.collections.ArrayList

class PreviousGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_games)
        val recyclerview = findViewById<RecyclerView>(R.id.rcPreviousGames)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val data = readPLacarDataSharedPreferences()
        val adapter = CustomAdapter(data)
        recyclerview.adapter = adapter
    }
    fun readPLacarDataSharedPreferences():ArrayList<Placar> {
        Log.v ("PDM", "Lendo o Shared Preferences")
        val data = ArrayList<Placar> ()
        val sharedFileName = "PreviousGames"
        var aux: String
        val sp: SharedPreferences = getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
        if(sp!=null) {
            val numMatches= sp.getInt("numberMatch", 0)

            for (i in 1.. numMatches){
                aux= sp.getString("match"+i,"vazio")!!
                if(!aux.equals("vazio")){

                    var bis:ByteArrayInputStream
                    bis = ByteArrayInputStream(aux.toByteArray(Charsets.ISO_8859_1))
                    var obi:ObjectInputStream
                    obi = ObjectInputStream(bis)

                    val placar: Placar = obi.readObject() as Placar
                    data.add(placar)

                }
            }
        }
        return data
    }
    fun clearHistory(view: View) {
        val recyclerview2 = findViewById<RecyclerView>(R.id.rcPreviousGames)
        val sharedFileName = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)
        sp.edit().clear().apply()
        val data = readPLacarDataSharedPreferences()
        val adapter = CustomAdapter(data)
        recyclerview2.adapter = adapter
    }

}
