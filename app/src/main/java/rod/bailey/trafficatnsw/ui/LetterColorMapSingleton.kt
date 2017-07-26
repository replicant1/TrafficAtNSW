package rod.bailey.trafficatnsw.ui

import android.content.Context
import android.support.annotation.ColorRes
import android.util.Log
import rod.bailey.trafficatnsw.R
import java.util.*

/**
 * Created by rodbailey on 26/7/17.
 */
class LetterColorMapSingleton private constructor(){

	private val letterToColorMap: MutableMap<Char, Int> = HashMap<Char, Int>()

	/** Colors from this list are awarded to characters in a cyclic manner */
	private val colorList: MutableList<Int> = LinkedList<Int>()

	private var colorListIndex:Int = 0

	init {
		colorList.add(R.color.palette_rusty_red)
		colorList.add(R.color.palette_sky_blue)
		colorList.add(R.color.palette_aqua)
		colorList.add(R.color.palette_cyan)
		colorList.add(R.color.palette_dark_green)
		colorList.add(R.color.palette_sky_blue)
		colorList.add(R.color.palette_dark_grey)
		colorList.add(R.color.palette_ghost_grey)
		colorList.add(R.color.palette_lawn_green)
		colorList.add(R.color.palette_light_mauve)
		colorList.add(R.color.palette_light_orange)
		colorList.add(R.color.palette_sea_green)
		colorList.add(R.color.palette_tawny_red)
	}

	private object Holder {
		val INSTANCE = LetterColorMapSingleton()
	}

	@ColorRes
	fun getColorForLetter(ctx: Context, ch: Char): Int {
		val key:Char = ch.toUpperCase()
		if (!letterToColorMap.containsKey(key)) {
			val color:Int = nextColor(ctx)
			Log.d(LOG_TAG, "key=${key}, color=${color}")
			letterToColorMap.put(key, color)
		}
		return letterToColorMap.get(key) ?: R.color.palette_rusty_red
	}

	private fun nextColor(ctx: Context): Int {
		val nextListIndex: Int = if (colorListIndex == colorList.size) 0 else colorListIndex++
		return colorList[nextListIndex]
	}

	companion object {
		private val LOG_TAG = LetterColorMapSingleton::class.java.simpleName
		val instance: LetterColorMapSingleton by lazy { Holder.INSTANCE }
	}
}