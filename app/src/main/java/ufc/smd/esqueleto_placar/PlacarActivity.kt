package ufc.smd.esqueleto_placar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

class PlacarActivity : AppCompatActivity() {
    lateinit var placar:Placar
    var timeIs = 0
    var ultimoGol = mutableListOf<Boolean>()
    val placar1 = mutableListOf<String>()
    val placar2 = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        placar1.add(0.toString())
        placar2.add(0.toString())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)
        val simpleChronometer = findViewById<Chronometer>(R.id.simpleChronometer)
        simpleChronometer.start()
        var t1 = findViewById<TextView>(R.id.time1)
        var t2 = findViewById<TextView>(R.id.time2)
        placar = getIntent().getExtras()?.getSerializable("placar") as Placar
        if (placar.has_timer){
            simpleChronometer.setVisibility(View.VISIBLE)
        } else {
            simpleChronometer.setVisibility(View.INVISIBLE)
        }
        val matchName= findViewById<TextView>(R.id.tvNomePartida2)
        matchName.text=placar.nome_partida
        ultimoJogos()
    }
    fun undo(v: View){

        var t1 = findViewById<TextView>(R.id.time1)
        var t2 = findViewById<TextView>(R.id.time2)
        if (ultimoGol.last()){
            ultimoGol.removeLast()
            placar1.removeLast()
            t1.text = placar1.last()
        }
        else {
            ultimoGol.removeLast()
            placar2.removeLast()
            t2.text = placar2.last()
        }

    }
    fun gol1(v: View) {

        var time1 = findViewById<TextView>(R.id.time1)
        var t1 = time1.text.toString().toInt()
        t1 += 1
        time1.text = t1.toString()
        placar1.add(t1.toString())
        ultimoGol.add(true)
        }
    fun gol2(v: View) {
        var time2 = findViewById<TextView>(R.id.time2)
        var t2 = time2.text.toString().toInt()
        t2 += 1
        time2.text = t2.toString()
        placar2.add(t2.toString())
        ultimoGol.add(false)
    }
    fun saveGame(v: View) {

        var t1 = findViewById<TextView>(R.id.time1)
        var t2 = findViewById<TextView>(R.id.time2)
        var placarAtual = t1.text.toString()+"x"+t2.text.toString()
        placar.resultado = placarAtual
        placar.resultadoLongo = placarAtual
        val sharedFilename = "PreviousGames"
        val sp: SharedPreferences = getSharedPreferences(sharedFilename, Context.MODE_PRIVATE)
        var edShared = sp.edit()
        //Salvar o número de jogos já armazenados
        var numMatches= sp.getInt("numberMatch",0) + 1
        edShared.putInt("numberMatch", numMatches)

        //Escrita em Bytes de Um objeto Serializável
        var dt= ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt);
        oos.writeObject(placar);

        //Salvar como "match"
        edShared.putString("match"+numMatches, dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit() //Não esqueçam de comitar!!!

        val intent = Intent(this, MainActivity::class.java).apply{}
        startActivity(intent)
    }
    fun ultimoJogos () {
        val sharedFilename = "PreviousGames"
        val sp:SharedPreferences = getSharedPreferences(sharedFilename,Context.MODE_PRIVATE)
        var matchStr:String=sp.getString("match1","").toString()
       // Log.v("PDM22", matchStr)
        if (matchStr.length >=1){
            var dis = ByteArrayInputStream(matchStr.toByteArray(Charsets.ISO_8859_1))
            var oos = ObjectInputStream(dis)
            var prevPlacar:Placar = oos.readObject() as Placar
            Log.v("PDM22", "Jogo Salvo:"+ prevPlacar.resultado)
        }

    }
    fun openPreviousGames(v: View) {
        val intent = Intent(this, PreviousGamesActivity::class.java).apply {
        }
        startActivity(intent)
    }
    fun pauseTime(v: View) {
       val c = findViewById<Chronometer>(R.id.simpleChronometer)
       timeIs = (c.getBase() - SystemClock.elapsedRealtime()).toInt();
       c.stop()
       findViewById<Button>(R.id.pausa).setVisibility(View.INVISIBLE)
       findViewById<Button>(R.id.despausa).setVisibility(View.VISIBLE)
    }
    fun unpauseTime(v: View) {
        val c = findViewById<Chronometer>(R.id.simpleChronometer);
        c.setBase(SystemClock.elapsedRealtime() + timeIs);
        c.start()
        findViewById<Button>(R.id.despausa).setVisibility(View.INVISIBLE)
        findViewById<Button>(R.id.pausa).setVisibility(View.VISIBLE)
    }}
