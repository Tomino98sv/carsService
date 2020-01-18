//zadefinovanie ake knižnice používame
const TokenGenerator = require('uuid-token-generator');
const cors=require('cors');
const express = require('express');
const db=require('./database');
const fs = require('fs');
const path=require('path');
const router=express.Router();
const multer=require('multer');
const resize = require('./resize');

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
        console.log(data);
        console.log(data.status);
        console.log(data.message);
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

app.get('/getbrands',(req,res)=>{
    console.log("Request on /getbrands :");
    db.getBrands(data=>{
        //console.log("tusom");
        res.status(data.status).send(data.message);
    });
});

app.post('/getmodels',(req,res)=>{
    console.log("Request on /getmodels :");
    db.getmodels(req.body.brand,data=>{
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

app.post('/changefirstname',(req,res)=>{
    console.log("Request on /changefirstname");
    db.changeFirstName(req.body,data=>{
        res.status(data.status).send(data.message);
    });
});

app.post('/changelastname',(req,res)=>{
    console.log("Request on /changelastname");
    db.changeSurname(req.body,data=>{
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

/*app.post('/getcarprofileimagethumb',(req,res)=>{
    console.log("Request on /getcarprofileimage");
    db.getCarProfileImage(req.body,data=>{
        const width =200;
        const height = 200;
        const format = 'png';

        console.log(format);
        res.type(`image/${format || 'png'}`)
        resize(data, format, width, height).pipe(res);
    });
});*/

app.post('/getcarimages',(req,res)=>{
    console.log("Request on /getcarimages");
    db.getcarimages(req.body,data=>{
        res.status(data.status).send(data.message);
    });
});

app.put("/setcarprofileimg",(req,res)=>{
    console.log("Request on /setCarProfileImg");
    db.setProfilePic(req.body.picture,req.body.car,data=>{
        res.status(data.status).send(data.message);
    });
});

var multipartUpload = multer({storage: multer.diskStorage({
    destination: function (req, file, callback) { callback(null, '/var/www/html/students2n/krendzelakm/public/images');},
    filename: function (req, file, callback) { callback(null, file.fieldname + '-' + Date.now()+path.extname(file.originalname));}})
}).array('image');


app.post('/sendimage',multipartUpload,(req,res)=>{
    console.log("Request on /sendimages");
    //console.log(req.body.carid);
    //console.log(req.file.filename);
    for(let i=0;i<req.files.length;i++){
        db.saveImages(req.body.carid,req.files[i].filename,data=>{
            console.log("Inserted "+i+" image");
            if(i==req.files.length-1){
                res.status(data.status).send(data.message);
            }
        });
    }
});

app.delete('/deleteimage',(req,res)=>{
    console.log("Request on /deleteimage");
    db.deleteImage(req.body.id,(data)=>{
        res.status(data.status).send(data.message);
    });
});

app.post('/getdocument',(req,res)=>{
    console.log("Request on /getdocument");
    db.getPdf(req.body.id,data=>{
        res.status(data.status).send(data.message);
    })
});

app.listen(1203,()=>{
    db.initializetokens();
    console.log("Tokens initialized!");
    console.log("Sever listening on port 1203");
});