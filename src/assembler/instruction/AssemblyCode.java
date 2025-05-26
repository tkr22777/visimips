
/**
 * @author 
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler.instruction;
public class AssemblyCode {
    String fullCode;
    int n;
    String[] lines;
    String[] label;
    Instruction[] instructions;
    String[] Registers = {
        "$zero",
        "$at",
        "$v0",
        "$v1",
        "$a0",
        "$a1",
        "$a2",
        "$a3",
        "$t0",
        "$t1",
        "$t2",
        "$t3",
        "$t4",
        "$t5",
        "$t6",
        "$t7",
        "$s0",
        "$s1",
        "$s2",
        "$s3",
        "$s4",
        "$s5",
        "$s6",
        "$s7",
        "$t8",
        "$t9",
        "$k0",
        "$k1",
        "$gp",
        "$sp",
        "$fp",
        "$ra"
    };

    // ignored   "sll 00 00" , "srl 00 02" ,
    String[] RTypeMnemonics = {
        "add 00 32",
        "sub 00 34",
        "and 00 36",
        "or 00 37",
        "nor 00 39" ,
        "slt 00 42",
        "mult 00 24",
        "div 00 26"
    };

    //ignored "bne 5","andi 12","ori 13","xori 14"};
    String[] ITypeMnemonics = {
        "beq 4" ,
        "addi 8" ,
        "lw 35" ,
        "sw 43"
    };

    String errors = "";
    
    AssemblyCode(String input) {
        this.fullCode = input;
        this.n = this.fillLines();        
        this.label = new String[n];
        this.instructions = new Instruction[n];
        for (int i = 0; i < n; i++){
            this.label[i] = "@NO LABEL@";
            this.instructions[i] = new Instruction();
        }

        if (!this.buildAndRemoveLabels()) {
            errors += "There is something wrong with the label" + "\n";
        }

        for (int i = 0 ; i < n ; i++) {
            this.convertAssemblyToMachine(this.lines[i], i);           
        }
    }

    int fillLines() {
        String[] temp = this.fullCode.split("\n");
        int j = 0;
        for(int i = 0; i < temp.length; i++) {
            temp[i] = temp[i].split("#")[0].trim();
            if (!temp[i].isEmpty()) {
                j++;
            }
        }
        this.lines = new String[j];
        int k = 0;
        int i = 0;
        while (k < j) {
           if (!temp[i].isEmpty()) {
               this.lines[k] = temp[i].trim();
               k++;
           }
           i++;
        }            
        return this.lines.length;
    }

    void printCode() {
        for (int i=0; i < n; i++) {
            String str = "";
            str = "" + (i+1) + " " + this.lines[i] + ";\t";
            if (str.length() < "9 addi $t5 , $t4 , 500;	 ".length()) {
                str = str + "\t";
            }
            str = str + " Decoded : ";
            System.out.print(str);
            this.instructions[i].printInstruction();
        }       
    }

    void printLabels(){
        for (int i = 0; i <n; i++) {
            System.out.println(this.label[i]);
        }
    }

    boolean buildAndRemoveLabels() {
        for (int i = 0; i < n; i++) {
            if (this.lines[i].split(":").length == 2) {
                this.label[i] = this.lines[i].split(":")[0].trim();
                this.lines[i] = this.lines[i].split(":")[1].trim();
            } else if (this.lines[i].split(":").length > 2) {
                return false;
            }
        }
        return true;
    }

    //Following function assumes there was no syntax error
    void convertAssemblyToMachine(String ins, int id){
        if(ins.equalsIgnoreCase("NOP")){
            this.instructions[id].setType('N');
            this.instructions[id].setFunction(0);
            this.instructions[id].setOffsetIJ((short)0);
            this.instructions[id].setRd(0);
            this.instructions[id].setRs(0);
            this.instructions[id].setRt(0);
            this.instructions[id].setShamt(0);
            this.instructions[id].setOp(0);
            this.instructions[id].iString = "NO OPERATION";
            return;            
        }
        
        String[] parts = ins.split(",| ");
        int j = 0;
        for(int i = 0 ; i < parts.length ; i++){
            parts[i] = parts[i].trim();
            if(!parts[i].isEmpty())
                j++;
        }
        String iParts[] = new String[j];
        j=0;
        for(int i = 0 ; i < parts.length ; i++){
            if(!parts[i].isEmpty()) {
                iParts[j] = parts[i];
                j++;
            }
        }
        for(int i = 0 ; i < iParts.length ; i++){
            if(i==0)
               this.instructions[id].iString = iParts[i];
            else if(i ==1)
               this.instructions[id].iString += " " + iParts[i];
            else
                this.instructions[id].iString = this.instructions[id].iString + ", " + iParts[i];
        }
        boolean con = true;
        //For a J type MIPS32 Instruction
        if(iParts.length == 2) {
           // System.out.println(iParts[0] + "__" + iParts[1]);           
            if(iParts[0].equalsIgnoreCase("j")) {
                this.instructions[id].setOp(2);
                this.instructions[id].setType('J');
            } else if(iParts[0].equalsIgnoreCase("jal")) {
                this.instructions[id].setOp(3);
                this.instructions[id].setType('J');
            } else {
                errors += "Err01: Mnemonic/Syntax Error in Assembly Code line : " + (id+1) + "\n";
                con = false;
            }
            if(containsOnlyNumbers(iParts[1]))
               this.instructions[id].setOffsetIJ((short)Integer.parseInt(iParts[1]));//short is used for 16 bit offset
            else {
                    for(int i = 0 ; i < this.n ; i++) {
                        if(this.label[i].equalsIgnoreCase(iParts[1])) {
                             this.instructions[id].setOffsetIJ((short)i); //short is used for 16 bit offset
                             break;
                        }
                        if(i==this.n-1) {
                            errors += "Err02: Jump Label Error in Assembly Code line : " + (id+1) + "\n";
                            con = false;
                        }
                    }
            }
        } else if(iParts.length == 3||( iParts.length == 4 && containsOnlyNumbers(iParts[3]))){
          //  System.out.println(iParts[0] + "__" + iParts[1]+ "__" + iParts[2]);            
            this.instructions[id].setType('I');            
            /*if(iParts[0].equalsIgnoreCase("lw"))
                this.instructions[id].setOp(35);
            else if(iParts[0].equalsIgnoreCase("sw"))
                this.instructions[id].setOp(43);
            else */

            if(this.checkIType(iParts[0])) {
                int op = -1;                
                for(int i = 0 ; i < this.ITypeMnemonics.length ; i++) {
                    if(iParts[0].equalsIgnoreCase(this.ITypeMnemonics[i].split(" ")[0])) {
                        op = Integer.parseInt(this.ITypeMnemonics[i].split(" ")[1]);
                        break;
                    }
                }
                this.instructions[id].setOp(op);
            } else {
                errors += "Err03: Mnemonic/Syntax Error in Assembly Code line : " + (id+1) + "\n";
                con = false;
            }
            int k = this.regNumOf(iParts[1]);  
            if(iParts[0].equalsIgnoreCase("lw")||iParts[0].equalsIgnoreCase("sw"))  {
                     //destination reg for I type rt setting first operand
                    if(k != -1)
                        this.instructions[id].setRt(k);
                    else  {
                        errors += "Err04: Error in target operand in Assembly Code line : " + (id+1)  + "\n";
                        con = false;
                    }
                    //setting offset for I type instruction
                    String offset = iParts[2].substring(0,iParts[2].indexOf('('));
                    if(containsOnlyNumbers(offset))
                        this.instructions[id].setOffsetIJ((short)Integer.parseInt(offset));//short is used for 16 bit offset
                    else  {
                        errors += "Err05: Error in Offset in Assembly Code line : " + (id+1) + "\n";
                        con = false;
                    }
                    //setting the rs for the I type instruction
                    iParts[2] = iParts[2].substring(iParts[2].indexOf('(')+1,iParts[2].length()-1);
                    k = this.regNumOf(iParts[2]);     
                    if(k != -1)
                        this.instructions[id].setRs(k);
                    else {
                        errors += "Err06: Error in target operand in Assembly Code line : " + (id+1) + "\n";
                        con = false;
                    }
            } else if(this.checkIType(iParts[0])) {
                //destination reg for I type rs/rt setting first operand
                k = this.regNumOf(iParts[1]);     
                if(k != -1) {
                    if(iParts[0].equalsIgnoreCase("beq")||iParts[0].equalsIgnoreCase("bne"))
                        this.instructions[id].setRs(k);
                    else
                        this.instructions[id].setRt(k);
                } else {
                    errors += "Err07: Error in rs operand in Assembly Code line : " + (id+1) + "\n";
                    con = false;
                }
                //destination reg for I type rt setting first operand
                k = this.regNumOf(iParts[2]);     
                if(k != -1)  {
                    if(iParts[0].equalsIgnoreCase("beq")||iParts[0].equalsIgnoreCase("bne"))
                        this.instructions[id].setRt(k);
                    else
                        this.instructions[id].setRs(k);
                } else{
                    errors += "Err08: Error in rt operand in Assembly Code line : " + (id+1) + "\n";
                    con = false;
                }
                
                //setting offset for I type                
                if(containsOnlyNumbers(iParts[3]))
                    this.instructions[id].setOffsetIJ((short)Integer.parseInt(iParts[3]));//short is used for 16 bit offset
                else if((iParts[0].equalsIgnoreCase("beq")||iParts[0].equalsIgnoreCase("bne"))
                        &this.returnLine(iParts[3])>=0)
                        this.instructions[id].setOffsetIJ((short)Integer.parseInt(iParts[3]));
                else
                {
                    errors += "Err09: Error in Offset in Assembly Code line : " + (id+1) + "\n";
                    con = false;
                }   
            } else {
                errors += "Err10: Mnemonic/Syntax Error in Assembly Code line : " + (id+1) + "\n";
                con = false;
            }
        }
        //For R type MIPS32 Instruction
        else if(iParts.length == 4) {

            //System.out.println(iParts[0] + "__" + iParts[1]+ "__" + iParts[2] + "__" + iParts[2]);
            if(checkRType(iParts[0])) {
                this.instructions[id].setType('R'); 
                this.instructions[id].setOp(0);
                //destination reg for R type rd setting first operand
                int k = this.regNumOf(iParts[1]);     
                if(k != -1)
                    this.instructions[id].setRd(k);
                else {
                    errors += "Err11: Error in rd operand in Assembly Code line : " + (id+1) + "\n";
                    con = false;
                }
                //destination reg for R type rs setting first operand
                k = this.regNumOf(iParts[2]);     
                if(k != -1)
                    this.instructions[id].setRs(k);
                else {
                    errors += "Err12: Error in rs operand in Assembly Code line : " + (id+1) + "\n";
                    con = false;
                }

                //destination reg for R type rt setting first operand
                k = this.regNumOf(iParts[3]);     
                if(k != -1)
                    this.instructions[id].setRt(k);
                else {
                    errors += "Err13: Error in rt operand in Assembly Code line : " + (id+1) + "\n";
                    con = false;
                }

                //setting Shamt and function
                int sft = -1;
                int func = -1;
                
                for(int i = 0 ; i < this.RTypeMnemonics.length ; i++) {
                    if(iParts[0].equalsIgnoreCase(this.RTypeMnemonics[i].split(" ")[0])) {
                        sft = Integer.parseInt(this.RTypeMnemonics[i].split(" ")[1]);
                        func = Integer.parseInt(this.RTypeMnemonics[i].split(" ")[2]);
                        break;
                    }
                }
                this.instructions[id].setShamt(sft);
                this.instructions[id].setFunction(func);
            } else {
                errors += "Err14: Mnemonic/Syntax Error in Assembly Code line : " + (id+1) + "\n";
                con = false;
            }
        } else {
            errors += "Err15: Syntax Error in Assembly Code line : " + (id+1) + " Parse Error!"  + "\n";
        }
        if(!con)
            this.instructions[id] = new Instruction();
        else
            System.out.print(errors);
    }
    
    boolean containsOnlyNumbers(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            //If we find a non-digit character we return false.
            if (!Character.isDigit(str.charAt(i)) && str.charAt(0)!='-') {
                return false;
            }
        }
        return true;
    }
    
    int regNumOf(String reg) {
        for (int i = 0 ; i < this.Registers.length ; i++) {
            if (this.Registers[i].equalsIgnoreCase(reg)) {
                return i;
            }
        }
        return -1;
    }

    boolean checkRType(String mne) {
        for (int i = 0 ; i < this.RTypeMnemonics.length ; i++) {
            if (mne.equalsIgnoreCase(this.RTypeMnemonics[i].split(" ")[0])) {
                return true;
            }
        }
        return false;
    }

    boolean checkIType(String mne) {
        for (int i = 0 ; i < this.ITypeMnemonics.length; i++) {
            if (mne.equalsIgnoreCase(this.ITypeMnemonics[i].split(" ")[0])) {
                return true;
            }
        }       
        return false;
    }

    int returnLine(String str) {
        for (int i=0; i < this.label.length ; i++) {
            if (this.label[i].equalsIgnoreCase(str)) {
                return i;
            }
        }
        return -1;
    }
}