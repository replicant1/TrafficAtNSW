package rod.bailey.trafficatnsw.hazard.ui

import android.support.annotation.ColorRes
import rod.bailey.trafficatnsw.R
import java.util.*

/**
 * Singleton that provides a mapping from a character to a color from an internally
 * defined palette. If a character C results in a color R, then the next time
 * you ask for the color of the same character C then you will once again get R. This
 * ensures the same colored circle is used for the same character, as required by
 * the Material Design guidelines for the circular row icons with a character in the
 * middle of the circle. Case is not significant.
 */
class LetterColorMapSingleton private constructor() {

	private val letterToColorMap: MutableMap<Char, Int> = HashMap<Char, Int>()

	/** Colors from this list are awarded to characters in a cyclic manner */
	private val colorList: MutableList<Int> = LinkedList<Int>()

	private var colorListIndex: Int = 0

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
	fun getColorForLetter(ch: Char): Int {
		val key: Char = ch.toUpperCase()
		if (!letterToColorMap.containsKey(key)) {
			val color: Int = nextColor()
			letterToColorMap.put(key, color)
		}
		return letterToColorMap.get(key) ?: R.color.palette_rusty_red
	}

	private fun nextColor(): Int {
		val nextListIndex: Int = if (colorListIndex == colorList.size) 0 else colorListIndex++
		return colorList[nextListIndex]
	}

	companion object {
		val instance: LetterColorMapSingleton by lazy { Holder.INSTANCE }
	}
}