//zadefinovanie ake knižnice používame
const TokenGenerator = require('uuid-token-generator');
const cors=require('cors');
const express = require('express');
const db=require('./database');
const fs = require('fs');
const path=require('path');
const router=express.Router();
const multer=require('multer');

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

app.post('/sendcodeforchangepassword',(req,res)=>{
    console.log("Request on /sendCodeForChangePassword");
    db.sendCodeForChangePassword(req.body,data=>{
        res.status(data.status).send(data.message);
    });
});

app.post('/abletochangepassword',(req,res)=>{
    console.log("Request on /ableToChangePassword");
    db.ableToChangePassword(req.body,data=>{
        res.status(data.status).send(data.message);
    });
});

app.post('/changepassword',(req,res)=>{
    console.log("Request on /changepassword");
    db.changePassword(req.body,data=>{
        res.status(data.status).send(data.message);
    });
});

app.post('/getcarprofileimage',(req,res)=>{
    console.log("Request on /getcarprofileimage");
    db.getCarProfileImage(req.body,data=>{
        let file=data.message;
        var type = 'image/jpeg' || 'image/png' || 'text/plain';
        var s = fs.createReadStream(file);
        s.on('open', function () {
            res.set('Content-Type', type);
            s.pipe(res);
        });
        s.on('error', function () {
            res.set('Content-Type', 'text/plain');
            res.status(data.status).send(data.message);
        });
    
    });
});

app.post('/getcarimages',(req,res)=>{
    console.log("Request on /getcarimages");
    db.getcarimages(req.body,data=>{
        let files=data.message;
        //TODO
    });
});

var multipartUpload = multer({storage: multer.diskStorage({
    destination: function (req, file, callback) { callback(null, './public');},
    filename: function (req, file, callback) { callback(null, file.fieldname + '-' + Date.now()+path.extname(file.originalname));}})
}).array('image');


app.post('/sendimage',multipartUpload,(req,res)=>{
    console.log("Request on /sendimages");
    //console.log(req.body.carid);
    //console.log(req.file.filename);
    for(let i=0;i<req.files.length;i++){
        db.saveImages(req.body.carid,"./public/"+req.files[i].filename,data=>{
            console.log("Inserted "+i+" image");
            if(i==req.files.length-1){
                res.status(data.status).send(data.message);
            }
        });
    }
});

app.listen(1203,()=>{
    db.initializetokens();
    console.log("Tokens initialized!");
    console.log("Sever listening on port 1203");
});