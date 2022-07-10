import java.io.File

var inputString:String ="";         // porgram statments
var currentInputIndex:Int = -1;
val ParserTokens = arrayListOf<String>();
var currentToken :String="";
var ScannerErrorFlag = false;
var lineNumber= 1;

fun getCrrentToken() : String{
    return currentToken;
}

fun match(expectedToken : String){
    if( getCrrentToken().equals(expectedToken)){
        getNextToken();
    }
    else{
        println("Syntax Error expected "+expectedToken );
        getNextToken();
    }
}
fun Program(){
    getNextToken();
    parseStatmentSequence();
}
fun hasNext():Boolean{
    if(currentInputIndex+1 < ParserTokens.size)
        return true;
    return false;
}

fun getNextToken():String{
    if(hasNext()) {
        currentToken = ParserTokens[++currentInputIndex];
        return ParserTokens[currentInputIndex];
    }
    else{
        return "";
    }
}

fun AddedFunction(){
   println("Added Function");   
}

fun newFucnction(){
  println("This is new Function);
}

fun parseStatmentSequence(){
    if(getCrrentToken() == "assign-statment") {
        match("assign-statment")
        parseAssignStatment();
    }
    else if(getCrrentToken() == "declaration-statment") {
        match("declaration-statment");
        parseDeclarationStament();
    }
    match(";");
    if(getCrrentToken() == "assign-statment" || getCrrentToken() == "declaration-statment")
        parseStatmentSequence(  );
}

fun parseAssignStatment(){
    match("data-type");
    match("identifier");
    match("=");
    parseExp();

}
fun parseExp(){

    parseTerm();
    while( getCrrentToken()  == "addop"){
        match(getCrrentToken());
        parseTerm();
    }

}

fun parseTerm(){
    parseFactor();
    while( getCrrentToken() == "mulop"){
        match(getCrrentToken());
        parseFactor();
    }
}

fun parseFactor() {
    when(getCrrentToken()){
        "(" ->{
            match("(");
            parseExp();
            match(")");
        }
        "number"->{
            match("number");
        }
        "identifier"->{
            match("identifier");
        }
        else ->{
            println("Syntax Error Expected expression");
        }
    }
}

fun parseDeclarationStament(){


    match("data-type");
    match("identifier");
}

fun parseDeclerationStatment(){
    var currentToken:String ="";
    if(hasNext()) {
        currentToken = getNextToken();
    }
    when(currentToken){
        "Identifier" -> {

        }
    }
}

fun isDelimiter(ch:Char):Boolean{
    if (ch == ' ' || ch == '+' || ch == '-' || ch == '*' || ch == ';' || ch == '=' || ch == '(' || ch == ')'||ch =='\n' )
        return (true);
    return (false);
}
fun isOperator(ch : Char):Boolean{
    if (ch == '+' || ch == '-' || ch == '*' || ch == '=')
        return (true);
    return (false);
}
fun isValidIdentifier(str:String) : Boolean{
    if(!Character.isLetter(str[0]))
        return false;
    var len = str.length;
    for ( i in 1 until len )
        if( (str[i] >='a' && str[i] <='z') || (str[i]>='A' && str[i] <='Z') ||(str[i]>='0' && str[i]<='9') )
            continue
        else
            return false;
    return true;

}
 fun isKeyWord(str:String):Boolean{
     if(str.equals("int") || str.equals("float"))
         return true;
     return false;
 }

fun isInteger(str:String):Boolean{
   var len = str.length;
    if(len == 0)        return false;
    for( item in str.orEmpty() ){
        if (item != '0' && item != '1' && item != '2'
            && item != '3' && item != '4' && item != '5'
            && item != '6' && item != '7' && item != '8'
            && item != '9')
            return (false);
    }
    return true;
}
fun isRealNumber(str:String):Boolean{
    var len = str.length;
    var hasDesimal = false;
    if(len == 0)        return false;
    for( item in str.orEmpty()){
        if (item != '0' && item != '1' && item != '2'
            && item != '3' && item != '4' && item != '5'
            && item != '6' && item != '7' && item != '8'
            && item != '9' && item != '.')
            return (false);
        if (item == '.')
            hasDesimal = true;
    }
    return hasDesimal;
}

fun shiftList(placeToSet:Int , statmentType:String){
    var len = ParserTokens.size;
    var temp = statmentType;
    for(i in placeToSet until len){
        var var2 = ParserTokens.get(i);
        ParserTokens.set(i , temp);
        temp = var2;
    }
    ParserTokens.add(";");
}

fun Scanner(){
    var token:String="";
    var left = 0;       //pointer to the left position
    var right = 0;      //pointer to the right position
    var startOfStatment = 0;
    var assignStatmentFlag = false;     // assign or declaration statment?
    val len = inputString.length;
    while( right < len && left <= right ){
        if(!isDelimiter(inputString[right]) )
            right++;
        if( ( (right< len) && (isDelimiter(inputString[right]) && left == right)) ){
            if(isOperator(inputString[right])) {
                if(inputString[right] == '+' || inputString[right] == '-')
                    ParserTokens.add("addop");
                else if(inputString[right] == '*')
                    ParserTokens.add("mulop");
                else if (inputString[right] == '=') {
                    assignStatmentFlag = true;
                    ParserTokens.add("=");
                }
            }
            else if(  right < len && isDelimiter(inputString[right])) {
                if(inputString[right].equals(';')){
                    var tkn: String = "";
                    tkn += inputString[right];
                    ParserTokens.add(tkn);
                    if(assignStatmentFlag) {
                        shiftList(startOfStatment , "assign-statment");           // add statment keyWord  in the startOfStatment, then shift the tokens to get the assign-statment keyword first
                        assignStatmentFlag = false;
                    }
                    else{
                        shiftList(startOfStatment , "declaration-statment");     // add statment keyWord  in the startOfStatment, then shift the tokens to get the declaration-statment keyword first
                    }
                    startOfStatment = ParserTokens.size;
                }
                else {
                   // println(inputString[right] + " is Delimiter");
                    var tkn: String = "";
                    tkn += inputString[right];
                    if(tkn != " " && tkn !="\n")
                        ParserTokens.add(tkn);
                }
            }
            right++;
            left = right;
        }
        else if( (right == len && left != right) || (isDelimiter(inputString[right]) && left != right) ){
            token = inputString.substring(left , right);

            if(isKeyWord(token)){
                //println(token+" is Key Word");
                ParserTokens.add("data-type");
            }
            else if(isInteger(token)){
                //println(token+"number");
                ParserTokens.add("number");
            }
            else if(isRealNumber(token)){
                //println(token+"number");
                ParserTokens.add("number");
            }
            else if(isValidIdentifier(token) && !isDelimiter(inputString[right -1])){
                //println(token+" is valid identifier");
                ParserTokens.add("identifier");
            }
            else if (!isValidIdentifier(token)  && !isDelimiter(inputString[right - 1]) ){
                ScannerErrorFlag = true;
                println("Scanner Error "+"Unidentified Token " + token);
            }
            left = right;
        }
    }
    if(ParserTokens.get(ParserTokens.size-1) != ";"){       // if uesr didnot enterd ";" at the end of the last statment we put it bardo xD
        ParserTokens.add(";");
        if(assignStatmentFlag) {
            shiftList(startOfStatment , "assign-statment");           // place to add "statment" Keywork
            assignStatmentFlag = false;
        }
        else{
            shiftList(startOfStatment , "declaration-statment");
        }
    }
}


fun main(args : Array<String>){
    inputString = File("file.txt").readText(Charsets.UTF_8);
    Scanner();      //Scanner
    //if(!ScannerErrorFlag)
    Program();      //Parser
}
