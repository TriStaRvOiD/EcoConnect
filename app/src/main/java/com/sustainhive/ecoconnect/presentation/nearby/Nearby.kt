package com.sustainhive.ecoconnect.presentation.nearby

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.common.MapboxOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.logo.generated.LogoSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import com.sustainhive.ecoconnect.BuildConfig
import com.sustainhive.ecoconnect.R
import com.sustainhive.ecoconnect.data.event.model.Region

@OptIn(MapboxExperimental::class, ExperimentalMaterial3Api::class)
@Composable
fun NearbyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        val region = Region(
            latitude = 19.044315,
            longitude = 73.025056
        )
        MapboxOptions.accessToken = BuildConfig.mapbox_public_token
        Box {
            if (isSystemInDarkTheme())
                MapboxMap(
                    mapViewportState = MapViewportState().apply {
                        setCameraOptions {
                            zoom(17.0)
                            center(Point.fromLngLat(region.longitude, region.latitude))
                            //                                pitch(0.0)
                            //                                bearing(0.0)
                        }
                    },
                    mapInitOptionsFactory = { context ->
                        MapInitOptions(
                            context = context,
                            styleUri = Style.DARK,
                        )
                    },
                    compassSettings = CompassSettings { enabled = false },
                    attributionSettings = AttributionSettings { enabled = false },
                    logoSettings = LogoSettings { enabled = false },
                    scaleBarSettings = ScaleBarSettings { enabled = false },
                    //                onMapLongClickListener = {
                    //                    locationServiceIntent.apply {
                    //                        action = LocationService.ACTION_LOCATION_TRACKING_DISABLE
                    //                        context.startService(this)
                    //                    }
                    //                    true
                    //                }
                ) {
                    AddPointer(
                        point = Point.fromLngLat(region.longitude, region.latitude),
                        context = LocalContext.current
                    )
                }
            else
                MapboxMap(
                    mapViewportState = MapViewportState().apply {
                        setCameraOptions {
                            zoom(17.0)
                            center(Point.fromLngLat(region.longitude, region.latitude))
                            pitch(0.0)
                            bearing(0.0)
                        }
                    },
                    mapInitOptionsFactory = { context ->
                        MapInitOptions(
                            context = context,
                            styleUri = Style.LIGHT,
                        )
                    },
                    attributionSettings = AttributionSettings { enabled = false },
                    logoSettings = LogoSettings { enabled = false },
                    scaleBarSettings = ScaleBarSettings { enabled = false },
//                onMapLongClickListener = {
//                    locationServiceIntent.apply {
//                        action = LocationService.ACTION_LOCATION_TRACKING_DISABLE
//                        context.startService(this)
//                    }
//                    true
//                }
                ) {
                    AddPointer(
                        point = Point.fromLngLat(region.longitude, region.latitude),
                        context = LocalContext.current
                    )
                }
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = "",
                        onQueryChange = {

                        },
                        onSearch = {

                        },
                        expanded = false,
                        onExpandedChange = {

                        },
                        leadingIcon = {

                        },
                        trailingIcon = {

                        },
                    )
                },
                expanded = false,
                onExpandedChange = {

                },
            ) {}
        }
    }
}

@OptIn(MapboxExperimental::class)
@Composable
private fun AddPointer(point: Point, context: Context) {
    val drawableRes =
        if (isSystemInDarkTheme()) R.drawable.location_light else R.drawable.location_dark

    val drawable = ResourcesCompat.getDrawable(
        context.resources,
        drawableRes,
        LocalContext.current.theme
    )
    val bitmap = drawable!!.toBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    PointAnnotation(
        iconImageBitmap = bitmap,
        iconSize = 1.5,
        point = point,
    )
}