package rod.bailey.trafficatnsw.traveltime.data

/**
 * Constructs an immutable MotorwayConfig for a single motorway.
 *
 * @param motorway Enum element that identifies this motorway
 * @param motorwayName
 *            Abbreviated name of the motorway eg. "M1"
 *
 * @param forwardName
 *            The human name given to an arbitrarily determined 'forward'
 *            direction e.g. "Northbound"
 *
 * @param backgwardName
 *            The human name given to the opposing direction to the
 *            'forwardName' eg. "Southbound"
 *
 * @param forwardSegmentIdPrefix
 *            Travel time segments (each one corresponding to a GeoJSON
 *            feature) have IDs that begin with a common prefix character.
 *            e.g. Eastbound segments might be E1, E2, ... E5, ETOTAL
 *
 * @param backwardSegmentIdPrefix
 *            Travel time segments (each one corresponding to a GeoJSON
 *            featre) have IDs that begin with a common prefix character.
 *            eg. Westbound segments might be W1, W2, WTOTAL
 *
 * @param preferencesFileName
 *            The user has the option to manually exclude selected segments
 *            from the overall calculation of travel time by tapping on that
 *            segment's row in the travel time list. This preferences file
 *            records the ID of those segments currently excluded from the
 *            TOTAL calculation for this motorway. THis preserves the user's
 *            selections between invocations of this app.
 *
 * @param remoteJsonUrl
 *            The absolute URL of a remote JSON file which contains the
 *            latest travel times for this motorway. These are usually
 *            updated by RMS every 5 minutes or so.
 *
 * @param localJsonFileName
 *            For testing purposes, it is useful to load travel times from a
 *            local JSON file in the assets folder. This file is just a
 *            local copy of the file previously returned from
 *            'remoteJsonUrl'.
 */
data class MotorwayConfig(val motorway: Motorway,
						  val motorwayName: String,
						  val forwardName: String,
						  val backwardName: String,
						  val forwardSegmentIdPrefix: String,
						  val backwardSegmentIdPrefix: String,
						  val preferencesFileName: String,
						  val remoteJsonUrl: String,
						  val localJsonFileName: String)
