package com.example.listycity3

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ripple

@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    var updatedCityName by remember { mutableStateOf("") }
    var updatedProvinceName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<City?>(null) }

    Column (modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    showAddCityFields = !showAddCityFields
                    selectedCity = null
                }
            ) {
                Text("+")
            }
        }

        if(showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = ({ Text("City") }),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = ({ Text("Province") }),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            onAddCity(
                                City(
                                    name = newCityName,
                                    province = newProvinceName,
                                )
                            )
                        }
                        newCityName = ""
                        newProvinceName = ""
                        showAddCityFields=false
                    }
                ) {
                    Text("Add City")
                }
            }
        }

        if(selectedCity != null){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = updatedCityName,
                    onValueChange = { updatedCityName = it },
                    label = { Text("Edit City") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = updatedProvinceName,
                    onValueChange = { updatedProvinceName = it },
                    label = { Text("Edit Province") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (updatedCityName.isNotBlank() && updatedProvinceName.isNotBlank()) {
                            val updatedCity = City(
                                name = updatedCityName,
                                province = updatedProvinceName
                            )

                            //Anthropic, Claude, "What does 'Argument type mismatch: actual type is
                            //City?, but City was expected' mean, what is the difference between
                            //City? and City, and how do I fix it?", 2026-09-18
                            onUpdateCity(selectedCity!!, updatedCity)
                        }
                        updatedCityName = ""
                        updatedProvinceName = ""
                        selectedCity=null
                    }
                ) {
                    Text("Update")
                }
            }
        }

        // Anthropic, Claude, "How could I pass all the values from CityListScreen
        //to be used in CityRow for the onClick function when making the city
        //rows clickable", 2026-09-18
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                CityRow(
                    city = city,
                    onClick = {
                        selectedCity = city
                        updatedCityName = city.name
                        updatedProvinceName = city.province
                        showAddCityFields = false
                    }
                )

                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun CityRow(city: City, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
            onAddCity = {},
            onUpdateCity = { _, _ -> }
        )
    }
}