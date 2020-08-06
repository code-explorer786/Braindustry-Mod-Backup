const furnace=multiLib.extend(GenericCrafter,GenericCrafter.GenericCrafterEntity,"multi-forge",{
},{
_output:[
[[["braindustry-exometal",1]],null,null],
[[["braindustry-graphenite",1]],null,null],
],
_input:[
[[["thorium",1],["spore-pod",2],["plastanium",2]],null,null],
[[["silicon",1],["titanium",1],["graphite",2]],null,null],
],
craftTimes:[60,110],
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
furnace.health= 300;
furnace.craftEffect= Fx.pulverizeMedium;
furnace.updateEffect=Fx.plasticburn;