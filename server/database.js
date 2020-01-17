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

let con;

function handleDisconnect() {
    con = mysql.createConnection({
        host: "itsovy.sk",
        user: "carfa",
        password: "glproject",
        database: "carfa",
        port:3306,
        insecureAuth:true
    });
  
    con.connect(function(err) {              
      if(err) {                                     
        console.log('error when connecting to db:', err);
        setTimeout(handleDisconnect, 2000); 
      }          
      else{
          console.log("connected to DB");
      }                           
    });                                     
    con.on('error', function(err) {
      console.log('db error', err);
      if(err.code === 'PROTOCOL_CONNECTION_LOST') { 
        handleDisconnect();                        
      } else {                                     
        throw err;                               
      }
    });
  }
  
  handleDisconnect();






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
                var str = err.sqlMessage;
                let newStr=str.slice(
                    str.lastIndexOf(' ') + 1
                );
                callbackR({"status":403,"message":"This "+newStr+" is already used."});
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
        let sql = "SELECT id,first_name,last_name,email,confirmed from users "+
        "where login like '"+login+"' and password like SHA2('"+password+"',224);";
        con.query(sql,(err,res)=>{
            if (err) console.log(err);
            if(res===undefined|| res.length===0){
                callbackR({"status":404,"message":"Username with this email and password doesn't exist. Please create a account."});
            }
            else{
                console.log(res);
                if(res[0].confirmed===0){
                    callbackR({"status":401,"message":res[0].email});
                }
                else{
                    token=tokgen.generate();
                    con.query("DELETE from  tokens where idUser like ((SELECT id from users where login like '"+login+"'));",(err)=>{
                        if(err) console.log(err);

                    });
                    con.query("INSERT INTO tokens(idUser,token) VALUES((SELECT id from users where login like '"+login+"'),'"+token+"');",(err)=>{
                        if(err) console.log(err);
                    });
                    let json={token:{login:login,token:token},userinfo:res[0]};
                    const index = tokens.findIndex(x => x.login === login);
                    console.log(index);
                    if (index !== -1) tokens.splice(index, 1);
                    tokens.push(json.token);
                    console.log(tokens);
                    callbackR({"status":200,"message":json});
                } 
            }
        });
    },

    logout(data,callbackR){
        const index = tokens.findIndex(x => x.token === data.token);
        if (index !== -1){ 
            tokens.splice(index, 1)
            con.query("DELETE from tokens where idUser like ((SELECT id from users where login like '"+data.login+"'));",(err)=>{
                if(err) console.log(err);
            });
            console.log(tokens);
            callbackR({"status":200,"message":"Successfuly logged out."});
        }
        else{
            callbackR({"status":401,"message":"Wrong user token."});
        }
    },
    
    confirmAccount(data,callbackR){
        con.query("UPDATE users set confirmed=1 where email like '"+data.email+"' and code='"+data.code+"'; ",(err)=>{
            console.log(err);
            con.query("SELECT ROW_COUNT() as rows;",(err,res)=>{
                if(err) console.log(err);
                console.log(res);
                if(res[0].rows==1){
                    var mailOptions = {
                        from: 'authorizedservicebmw@gmail.com',
                        to: data.email,
                        subject: 'Welcome to Carfa , your car service!',
                        text:"Successfully confirmed account."
                    };
                    transporter.sendMail(mailOptions, function(error, info){
                        if (error) {
                          console.log(error);
                        } else {
                          console.log('Email sent: ' + info.response);
                        }
                      });
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
            if (err) console.log(err);
            else{
                if(res.length!==0){
                    for(i=0;i<res.length;i++){
                        tokens.push(JSON.parse(JSON.stringify(res[i])));
                    }
                    console.log(tokens);
                }
            }
        });
    },
//affected by change
    addcar(data,callbackR){
        let userID=data.userID;
        let brand=data.brand;
        let model=data.model;
        let color=data.color;
        let vintage=data.vintage;
        let kilometrage=data.kilometrage;
        let SPZ=data.spz;
        let fuel=data.fuel;
        let manualTrans=data.transmission;
        let volume=data.volume;
        let token=data.token;

        let selectSql="SELECT id from cars where brand like '"+brand+"' and model like '"+model+"';";

        if(tokens.some(x=>(x.token===token.token)&&(x.login===token.login))){
            con.query(selectSql,(err,res)=>{
                if(err) console.log(err);
                let sql="INSERT INTO cardetails(userid,SPZ,color,vintage,kilometrage,carid,fuel,manualtrans,volume)" +
                "VALUES("+userID+",'"+SPZ+"','"+color+"',"+vintage+","+kilometrage+","+res[0].id+",'"+fuel+"',"+manualTrans+","+volume+");";
                console.log(sql);
                con.query(sql,(err,res)=>{
                    if(err){
                        console.log(err);
                        callbackR({"status":403,"message":"Car with this motor vehicle registration plate already exists."});
                    }
                    else{
                        let idčko=res.insertId;
                        callbackR({"status":200,"message":{"id":idčko}});
                    } 
                });
            });
        }
        else{
            callbackR({"status":401,"message":"Wrong user token , please log in again."});
        }
    },

    //affected by change
    getAllCars(data,callbackR){
        let login=data.login;
        let token=data.token;
        if(tokens.some(x=>(x.token===token)&&(x.login===login))){
            let sql="SELECT cardetails.id,brand,model from cardetails inner join cars on cardetails.carid=cars.id where cardetails.userid=(SELECT id from users where login like'"+login+"');"
            con.query(sql,(err,res)=>{
                if(err) console.log(err);
                if(res.length===0){
                    callbackR({"status":403,"message":"No cars yet."});
                }
                else{
                    callbackR({"status":200,"message":res});
                }
            });
        }
        else{
            callbackR({"status":401,"message":"Wrong user token , please log in again."});
        }
    },

    getBrands(callbackR){
        let sql="SELECT DISTINCT brand from cars order by brand";
        con.query(sql,(err,res)=>{
            if (err ) console.log(err);
            if(res.length===0){
                callbackR({"status":404,"message":"0 cars in database."});
            }
            else{
                callbackR({"status":200,"message":res});
            }
        });
    },
    
    getmodels(brand,callbackR){
        let sql="SELECT model from cars where brand like '"+brand+"' order by model;";
        con.query(sql,(err,res)=>{
            if (err ) console.log(err);
            if(res.length===0){
                callbackR({"status":404,"message":"0 cars in database."});
            }
            else{
                callbackR({"status":200,"message":res});
            }
        });
    },

    sendCodeForChangePassword(data,callbackR){
        let email=data.email;
        let code=generateCode();
        con.query("SELECT id from users where email like '"+email+"';",(err,res)=>{
            if(err) console.log(err);
            if(res.length===0){
                callbackR({"status":404,"message":"User with this email doesn't exist."});
            }
            else{
                con.query("INSERT INTO forgotpasswordcodes VALUES(id,"+res[0].id+",'"+code+"');",(err)=>{
                    if(err){
                        //console.log(err);
                        con.query("update forgotpasswordcodes SET code='"+code+"' WHERE idUser="+res[0].id+";",(err,res)=>{
                            if(err) console.log(err);
                            var mailOptions = {
                                from: 'authorizedservicebmw@gmail.com',
                                to: email,
                                subject: 'Password recovery code',
                                text: "Your recovery code is :  "+code
                            };
                            transporter.sendMail(mailOptions, function(error, info){
                                if (error) {
                                  console.log(error);
                                } else {
                                  console.log('Email sent: ' + info.response);
                                  callbackR({"status":200,"message":"Code has been sent to the email.Please check your email."});
                                }
                            });
                        });
                    }
                    else{
                        var mailOptions = {
                            from: 'authorizedservicebmw@gmail.com',
                            to: email,
                            subject: 'Password recovery code',
                            text: "Your recovery code is :  "+code
                        };
                        transporter.sendMail(mailOptions, function(error, info){
                            if (error) {
                              console.log(error);
                            } else {
                              console.log('Email sent: ' + info.response);
                              callbackR({"status":200,"message":"Code has been sent to the email.Please check your email."});
                            }
                        });
                    }
                });
            }
        });
    },
    ableToChangePassword(data,callbackR){
        let email=data.email;
        let code=data.code;
        con.query("SELECT * from forgotpasswordcodes WHERE iduser=(SELECT id from users where email like '"+email+"') AND code like '"+code+"';",(err,res)=>{
            if(err) console.log(err);
            if(res.length===0){
                callbackR({"status":401,"message":"Wrong code."});
            }
            else{
                con.query("update forgotpasswordcodes set code='' where iduser=(SELECT id from users where email like '"+email+"')",(err)=>{
                    if(err) console.log(err);
                });
                callbackR({"status":200,"message":"Able to change password."})
            }
        });
    },

    changePassword(data,callbackR){
        let email=data.email;
        let password=data.password;

        con.query("UPDATE users SET password=SHA2('"+password+"',224) WHERE email like '"+email+"';",(err)=>{
            if(err){
                console.log(err);
                callbackR({"status":500,"message":"Could not change password"});
            }
            else{
                callbackR({"status":200,"message":"Successfully changed password."});
            }
        });
    },

    getCarProfileImage(data,callbackR){
        let carID=data.idcar;
    
        con.query("select path from imagepaths INNER JOIN carowners on imagepaths.id=carowners.profileimgid where carowners.id="+carID+";",(err,res)=>{
            if(err) console.log(err);
            if(res.length!==0){
                callbackR({"status":200,"message":"/var/www/html/students2n/krendzelakm/public/images/"+res[0].path});
            }
            else{
                callbackR({"status":404,"message":"Car not found."});
            }
        });
    },

    getcarimages(data,callbackR){
        let carID=data.idcar;
    
        con.query("select path from imagepaths where idcar="+carID+";",(err,res)=>{
            if(err) console.log(err);
            res=JSON.parse(JSON.stringify(res));
            console.log(res);
            
            const result=res.reduce((acc,value)=>
                [...acc,"http://itsovy.sk/students2n/krendzelakm/public/images/"+value.path]
            ,[]);
            console.log(result);
            if(res.length!==0){
                callbackR({"status":200,"message":result});
            }
            else{
                callbackR({"status":404,"message":"Car not found."});
            }
        });
    },

    saveImages(id,imagePath,callbackR){
        con.query("INSERT INTO imagepaths VALUES(id,'"+imagePath+"',"+id+");",(err)=>{
            if(err) {
                console.log(err);
                callbackR({"status":500,"message":"Could not upload photo."});
            }else{
                callbackR({"status":200,"message":"Successfully added new photo!"});
            }
        });
    }
}