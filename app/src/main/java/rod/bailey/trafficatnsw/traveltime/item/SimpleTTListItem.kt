package rod.bailey.trafficatnsw.traveltime.item

import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment

/** An ITTListItem that is not a heading but a travel time road segment  */
class SimpleTTListItem(val travelTime: XTravelTimeSegment) : ITTListItem