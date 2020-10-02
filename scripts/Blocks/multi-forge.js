const furnace=multiLib.extend(GenericCrafter,GenericCrafter.GenericCrafterEntity,"multi-forge",{
},{
_output:[
[[["braindustry-exotic-alloy",1]],null,null],
[[["braindustry-graphenite",1]],null,null],
],
_input:[
[[["thorium",1],["titanium",1],["plastanium",2]],null,null],
[[["silicon",2],["titanium",1],["graphite",1]],null,null],
],
craftTimes:[80,110],
//DON'T MODIFY THESE
output:[],
input:[],
itemList:[],
liquidList:[],
isSameOutput:[],
});
furnace.enableInv=false;
furnace.dumpToggle=false;
furnace.localizedName="Multi Forge";
furnace.description="Multicrafter with posibility craft Graphenite or Exometal.";
furnace.itemCapacity= 30;
furnace.liquidCapacity= 0;
furnace.size= 3;
furnace.health= 240;
furnace.craftEffect= Fx.pulverizeMedium;
furnace.updateEffect=Fx.plasticburn;
