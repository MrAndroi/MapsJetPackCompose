package com.example.mapscompose.presentaion.mapscreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mapscompose.R
import com.example.mapscompose.presentaion.viewmodel.MapsViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsPickerScreen(
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val constraints = ConstraintSet {
            val map = createRefFor("mapBox")
            val bottomSection = createRefFor("bottomSection")

            constrain(map){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }

            constrain(bottomSection){
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }

        }
        ConstraintLayout(constraints, Modifier.fillMaxSize()) {
            MapsPicker("mapBox")
            BottomSectionWrapper("bottomSection")
        }
    }
}

@Composable
fun MapsPicker(layoutId:String, viewModel:MapsViewModel = hiltViewModel()) {
    val constraints = ConstraintSet {
        val map = createRefFor("map")
        val locationMarker = createRefFor("locationMarker")

        constrain(map){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }

        constrain(locationMarker){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.value(40.dp)
            height = Dimension.value(40.dp)
        }
    }
    val properties = MapProperties(true)
    val uiSettings = MapUiSettings(
        myLocationButtonEnabled = true,
        zoomControlsEnabled = false
    )
    val cameraPositionState = rememberCameraPositionState()
    if (!cameraPositionState.isMoving){
        val latLng = cameraPositionState.position.target
        viewModel.getLocationInfo("${latLng.latitude},${latLng.longitude}")

    }

    val offsetY = animateDpAsState(
        if (cameraPositionState.isMoving) (-20).dp else 0.dp,
        animationSpec = tween(
            durationMillis = 200,
        )
    )

    val shadow = animateDpAsState(
        if (cameraPositionState.isMoving) (0).dp else 20.dp,
    )

    ConstraintLayout(constraints, Modifier.layoutId(layoutId)) {
        GoogleMap(
            modifier = Modifier.layoutId("map"),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        )
        Icon(
            imageVector = Icons.Default.LocationOn,
            tint = colorResource(id = R.color.red),
            contentDescription = null,
            modifier = Modifier
                .layoutId("locationMarker")
                .offset(y = offsetY.value)
                .shadow(shadow.value, CircleShape)
        )
    }

}

@Composable
fun BottomSectionWrapper(layoutId: String, viewModel:MapsViewModel = hiltViewModel()) {

    val locationInfo by remember {
        viewModel.currentLocationInfo
    }
    val isLoading by remember{
        viewModel.isLoading
    }
    val loadError by remember {
        viewModel.loadError
    }

    if (isLoading){
        BottomSection(isLoading = true, layoutId = layoutId, locationInfo = "")
    }
    else{
        if(loadError.isEmpty()){
            BottomSection(isLoading = false, layoutId = layoutId, locationInfo = locationInfo)
        }
        else{
            BottomSection(isLoading = false, layoutId = layoutId, locationInfo = locationInfo,loadError)
        }

    }

}

@Composable
fun BottomSection(isLoading: Boolean,layoutId: String,locationInfo:String,error:String?=null) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            .background(Color.White)
            .padding(20.dp)
            .layoutId(layoutId),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isLoading){
                CircularProgressIndicator()
            }
            else{
                Row(Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Text(
                        text = "Address: ",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = if(error.isNullOrEmpty()) locationInfo else error,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(id = R.color.text_gray)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                    ,
                    contentPadding = PaddingValues(16.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = colorResource(id = R.color.red)
                    )
                    Text(text = "Deliver Here!")
                }
            }

        }
    }
}