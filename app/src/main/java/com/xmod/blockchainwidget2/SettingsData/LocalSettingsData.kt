package com.sout.cryptocurrencytracker.SettingsData

import com.sout.cryptocurrencytracker.Model.SettingsCurrencyModel



class LocalSettingsData {

    companion object{

        fun data(): MutableList<SettingsCurrencyModel>{

            var currencyData = mutableListOf<SettingsCurrencyModel>(

           SettingsCurrencyModel("btc", currencyIndex=0), SettingsCurrencyModel("eth", currencyIndex=1), SettingsCurrencyModel("ada", currencyIndex=2), SettingsCurrencyModel("bnb", currencyIndex=3), SettingsCurrencyModel("usdt", currencyIndex=4), SettingsCurrencyModel("xrp", currencyIndex=5), SettingsCurrencyModel(currencyName="doge", currencyIndex=6), SettingsCurrencyModel(currencyName="dot", currencyIndex=7), SettingsCurrencyModel(currencyName="usdc", currencyIndex=8), SettingsCurrencyModel(currencyName="sol", currencyIndex=9), SettingsCurrencyModel(currencyName="uni", currencyIndex=10), SettingsCurrencyModel(currencyName="bch", currencyIndex=11), SettingsCurrencyModel(currencyName="link", currencyIndex=12), SettingsCurrencyModel(currencyName="ltc", currencyIndex=13), SettingsCurrencyModel(currencyName="busd", currencyIndex=14), SettingsCurrencyModel(currencyName="luna", currencyIndex=15), SettingsCurrencyModel(currencyName="matic", currencyIndex=16), SettingsCurrencyModel(currencyName="icp", currencyIndex=17), SettingsCurrencyModel(currencyName="wbtc", currencyIndex=18), SettingsCurrencyModel(currencyName="xlm", currencyIndex=19), SettingsCurrencyModel(currencyName="etc", currencyIndex=20), SettingsCurrencyModel(currencyName="vet", currencyIndex=21), SettingsCurrencyModel(currencyName="fil", currencyIndex=22), SettingsCurrencyModel(currencyName="theta", currencyIndex=23), SettingsCurrencyModel(currencyName="AVAX", currencyIndex=24), SettingsCurrencyModel(currencyName="trx", currencyIndex=25), SettingsCurrencyModel(currencyName="atom", currencyIndex=26), SettingsCurrencyModel(currencyName="okb", currencyIndex=27), SettingsCurrencyModel(currencyName="dai", currencyIndex=28), SettingsCurrencyModel(currencyName="ftt", currencyIndex=29), SettingsCurrencyModel(currencyName="ceth", currencyIndex=30), SettingsCurrencyModel(currencyName="eos", currencyIndex=31), SettingsCurrencyModel(currencyName="aave", currencyIndex=32), SettingsCurrencyModel(currencyName="xmr", currencyIndex=33), SettingsCurrencyModel(currencyName="grt", currencyIndex=34), SettingsCurrencyModel(currencyName="cake", currencyIndex=35), SettingsCurrencyModel(currencyName="cusdc", currencyIndex=36), SettingsCurrencyModel(currencyName="klay", currencyIndex=37), SettingsCurrencyModel(currencyName="axs", currencyIndex=38), SettingsCurrencyModel(currencyName="cdai", currencyIndex=39), SettingsCurrencyModel(currencyName="shib", currencyIndex=40), SettingsCurrencyModel(currencyName="neo", currencyIndex=41), SettingsCurrencyModel(currencyName="cro", currencyIndex=42), SettingsCurrencyModel(currencyName="algo", currencyIndex=43), SettingsCurrencyModel(currencyName="mkr", currencyIndex=44), SettingsCurrencyModel(currencyName="xtz", currencyIndex=45), SettingsCurrencyModel(currencyName="bsv", currencyIndex=46), SettingsCurrencyModel(currencyName="ksm", currencyIndex=47), SettingsCurrencyModel(currencyName="miota", currencyIndex=48), SettingsCurrencyModel(currencyName="steth", currencyIndex=49), SettingsCurrencyModel(currencyName="egld", currencyIndex=50), SettingsCurrencyModel(currencyName="btt", currencyIndex=51), SettingsCurrencyModel(currencyName="rune", currencyIndex=52), SettingsCurrencyModel(currencyName="amp", currencyIndex=53), SettingsCurrencyModel(currencyName="leo", currencyIndex=54), SettingsCurrencyModel(currencyName="sushi", currencyIndex=55), SettingsCurrencyModel(currencyName="waves", currencyIndex=56), SettingsCurrencyModel(currencyName="comp", currencyIndex=57), SettingsCurrencyModel(currencyName="near", currencyIndex=58), SettingsCurrencyModel(currencyName="dash", currencyIndex=59), SettingsCurrencyModel(currencyName="qnt", currencyIndex=60), SettingsCurrencyModel(currencyName="ht", currencyIndex=61), SettingsCurrencyModel(currencyName="cel", currencyIndex=62), SettingsCurrencyModel(currencyName="dcr", currencyIndex=63)

            )

            return currencyData
        }
    }

}