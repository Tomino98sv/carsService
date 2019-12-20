const mysql = require('mysql');
const express = require('express');
const TokenGenerator = require('uuid-token-generator');
const tokgen = new TokenGenerator(128, TokenGenerator.BASE62);


var nodemailer = require('nodemailer');
var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: 'authorizedservicebmw@gmail.com',
      pass: 'skap123654'
    }
  });


var tokens=new Array();

let con=mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "",
    database: "carfa",
    port: "3306"
});
con.connect();

let generateCode=()=>{
    return Math.floor(100000 + Math.random() * 900000);
}


module.exports={

    register(data,callbackR){
        let login=data.login;
        let password=data.password;        
        let fname=data.fname;
        let lname=data.lname;
        let email=data.email;
        let code=generateCode();
        //console.log("connected");
        let sql = "INSERT INTO users(login,password,first_name,last_name,email,code) "+
        "VALUES ('"+login+"',SHA2('"+password+"',224),'"+fname+"','"+lname+"','"+email+"','"+code+"');";
        //console.log(sql);
        con.query(sql,(err,res)=>{
            if (err){
                console.log(err);
                callbackR({"status":403,"message":"This user already exists"});
            }
            else{
                var mailOptions = {
                    from: 'authorizedservicebmw@gmail.com',
                    to: email,
                    subject: 'Email confirmation code',
                    text: "Your confirmation code is :  "+code
                };
                transporter.sendMail(mailOptions, function(error, info){
                    if (error) {
                      console.log(error);
                    } else {
                      console.log('Email sent: ' + info.response);
                    }
                  });
                callbackR({"status":200,"message":"New user: "+login+" successfully registered!"}); 
            }
        });
    },

    login(data,callbackR){
        let login=data.login;
        let password=data.password;        
        //console.log("connected");
        let sql = "SELECT confirmed from users "+
        "where login like '"+login+"' and password like SHA2('"+password+"',224);";
        console.log(sql);
        con.query(sql,(err,res)=>{
            if (err) console.log(err);
            if(res===undefined){
                callbackR({"status":401,"message":"Username with this email and password doesn't exist. Please create a account."});
            }
            else{
                if(res[0].confirmed===0){
                    callbackR({"status":401,"message":"please check email to confirm your account"});
                }
                else{
                    token=tokgen.generate();
                    con.query("DELETE from  tokens where idUser like ((SELECT id from users where login like '"+login+"'));",(err)=>{
                        if(err) console.log(err);
                    });
                    con.query("INSERT INTO tokens(idUser,token) VALUES((SELECT id from users where login like '"+login+"'),'"+token+"');",(err)=>{
                        if(err) console.log(err);
                    });
                    let json={login:login,token:token};
                    const index = tokens.findIndex(x => x.username === login);
                    console.log(index);
                    if (index !== -1) tokens.splice(index, 1);
                    tokens.push(json);
                    console.log(tokens);
                    callbackR({"status":200,"message":json});
                } 
            }
        });
    },

    logout(data,callbackR){
        const index = tokens.findIndex(x => x.token === data.token);
        if (index !== -1) tokens.splice(index, 1);
        con.query("DELETE from tokens where idUser like ((SELECT id from users where login like '"+data.login+"'));",(err)=>{
            if(err) console.log(err);
        });
        console.log(tokens);
        callbackR({"status":200,"message":"Successfuly logged out"});
    },
    
    confirmAccount(data,callbackR){
        con.query("UPDATE users set confirmed=1 where login like '"+data.login+"' and code='"+data.code+"'; ",(err)=>{
            console.log(err);
            con.query("SELECT ROW_COUNT() as rows;",(err,res)=>{
                if(err) console.log(err);
                console.log(res);
                if(res[0].rows==1){
                    callbackR({"status":200,"message":"User confirmed successfully."});
                }
                else{
                    callbackR({"status":401,"message":"Wrong code or user doesn't exist."});
                }
            });
        });
    },
    initializetokens(){
        con.query("SELECT users.login as login,token from tokens inner join users on tokens.iduser=users.id;",(err,res)=>{
            if (err)console.log(err);
            if(res.length!==0){
                for(i=0;i<res.length;i++){
                    tokens.push(JSON.parse(JSON.stringify(res[i])));
                }
                console.log(tokens);
            }
        });
    }
}