//zadefinovanie ake knižnice používame
const TokenGenerator = require('uuid-token-generator');
const cors=require('cors');
const express = require('express');
const db=require('./database');

//Definuje token generaciu(pomocou knižnice na tokeny)
const tokgen = new TokenGenerator(128, TokenGenerator.BASE62);
//console.log(tokgen);
var app=express();
app.use(express.json());
app.use(cors());


app.post('/register',(req,res)=>{
    console.log("Request on /register :");
    db.register(req.body,data=>{
        res.status(data.status).send(data.message);
    });
});

app.post('/login',(req,res)=>{
    console.log("Request on /login :");
    db.login(req.body,data=>{
        //console.log("tusom");
        res.status(data.status).send(data.message);
    });
});
app.post('/logout',(req,res)=>{
    console.log("Request on /logout :");
    db.logout(req.body,data=>{
        //console.log("tusom");
        res.status(data.status).send(data.message);
    });
});

app.post('/confirmuser',(req,res)=>{
    console.log("Request on /confirmuser :");
    db.confirmAccount(req.body,data=>{
        //console.log("tusom");
        res.status(data.status).send(data.message);
    });
});

app.post('/addcar',(req,res)=>{
    console.log("Request on /addcar :");
    db.addcar(req.body,data=>{
        //console.log("tusom");
        res.status(data.status).send(data.message);
    });
});

app.post('/getcars',(req,res)=>{
    console.log("Request on /getcars :");
    db.getAllCars(req.body,data=>{
        //console.log("tusom");
        res.status(data.status).send(data.message);
    });
});

app.listen(1203,()=>{
    db.initializetokens();
    console.log("Tokens initialized!");
    console.log("Sever listening on port 1203");
});