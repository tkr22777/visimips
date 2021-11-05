
/**
 * @author
 * Tahsin Kabir
 * M Tahmid Bari
 */
package assembler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MFrame implements Serializable {
    
    Instruction Ins;
    IF_ID_Pipeline ifIdPipeline;
    ID_EX_Pipeline idExPipeline;
    EX_MEM_Pipeline exMemPipeline;
    MEM_WB_Pipeline memRbPipeline;
    int[] register;
    boolean[] regFlag;
    int[] MEM;
    ForwardingUnit fwdUnit;
    HazardDetectionUnit hdUnit;
    ControlUnit cUnit;
    ALU ALUnit;
    //used for the visualization part
    String wbiString;
    int currentPC;
    int nextPC;

    MFrame() {
        register = new int[32];
        regFlag = new boolean[32];
        regFlag[0]=true;
        MEM = new int[1000];
        this.idExPipeline = null;
        this.ifIdPipeline = null;
        this.exMemPipeline = null;
        this.memRbPipeline = null;
        this.fwdUnit = null;
        this.hdUnit = null;
    }
    
    int insertInstruction(Instruction ins,int PC) {
        this.Ins = ins; // needed only for the visualization part
        this.currentPC = PC; // needed only for the visualization part
        fwdUnit = new ForwardingUnit();
        hdUnit = new HazardDetectionUnit();
        ALUnit = new ALU();
        this.wbiString = "NOP";

        if(this.memRbPipeline != null){
            //Required data to the forwarding unit from MEM/WB pipeline is passed
            this.fwdUnit.RegWriteMemWbAdd = this.memRbPipeline.destReg;
            this.fwdUnit.RegWriteMemWbFlag = this.memRbPipeline.WB.RegWrite;
            this.fwdUnit.RegWriteMemWbValue = 0;
            
            int WriteData;
            //Following if block does the writing to the reg library for any reg write instruction.
            if(this.memRbPipeline.WB.RegWrite) {
                if(this.memRbPipeline.WB.MemToReg)
                    WriteData= this.memRbPipeline.Mdata;
                else
                    WriteData= this.memRbPipeline.ALU_result;
                this.register[this.memRbPipeline.destReg] = WriteData;
                this.regFlag[this.memRbPipeline.destReg] = true;
                this.fwdUnit.RegWriteMemWbValue = WriteData;
            }
            //Data Needed for visualization part
            this.wbiString = this.memRbPipeline.iString;
            this.tempWbMData = this.memRbPipeline.Mdata;
            this.tempWbAluResult = this.memRbPipeline.ALU_result;
            this.tempWbRegWrite = this.memRbPipeline.WB.RegWrite;
            this.tempWbMemToReg = this.memRbPipeline.WB.MemToReg;
        }
        
        if(this.exMemPipeline!=null) {
            this.memRbPipeline = new MEM_WB_Pipeline();
            //This is where I passed required data to the forwarding unit from EX/MEM pipeline
            this.fwdUnit.RegWriteExMemAdd = this.exMemPipeline.destReg;
            this.fwdUnit.RegWriteExMemFlag = this.exMemPipeline.WB.RegWrite;
            this.fwdUnit.RegWriteExMemValue = this.exMemPipeline.ALU_result;
            this.hdUnit.branch = this.exMemPipeline.MEM.branch;
            this.hdUnit.ALUzero = this.exMemPipeline.ALU_zero;
            this.hdUnit.branchAddress = this.exMemPipeline.PCBranch;
                
            //Here datas from EX/MEM pipeline will come to MEM/WB pipeline
            this.memRbPipeline.WB = this.exMemPipeline.WB;   
            if(this.exMemPipeline.MEM.MemRead)
                this.memRbPipeline.Mdata = MEM[this.exMemPipeline.ALU_result];
            else
               this.memRbPipeline.Mdata = 0;            
            if(this.exMemPipeline.MEM.MemWrite)
                this.MEM[this.exMemPipeline.ALU_result]=this.exMemPipeline.MemDataWrite;           
            this.memRbPipeline.ALU_result = this.exMemPipeline.ALU_result;
            this.memRbPipeline.destReg = this.exMemPipeline.destReg;
            this.memRbPipeline.iString = this.exMemPipeline.iString;

            //Data Needed for visualization part
            this.tempMemMRead = this.exMemPipeline.MEM.MemRead;
            this.tempMemMWrite = this.exMemPipeline.MEM.MemWrite;
            this.tempMemWriteData = this.exMemPipeline.MemDataWrite;           
        } else
            this.memRbPipeline = null;        
        
        if(this.idExPipeline!=null) {
            this.hdUnit.idExMemRead = this.idExPipeline.MEM.MemRead;   
            this.hdUnit.idExRt = this.idExPipeline.rt;
            this.fwdUnit.rsValue = this.idExPipeline.rsValue;
            this.fwdUnit.rtValue = this.idExPipeline.rtValue;
            this.fwdUnit.rs = this.idExPipeline.rs;
            this.fwdUnit.rt = this.idExPipeline.rt;            
            
            this.exMemPipeline = new EX_MEM_Pipeline();          
            this.exMemPipeline.WB = this.idExPipeline.WB;
            this.exMemPipeline.MEM = this.idExPipeline.MEM;            
            this.exMemPipeline.PCBranch = this.idExPipeline.PC + this.idExPipeline.i32Offset*4;             
            int operand1 = this.fwdUnit.valueMuxA();
            int operand2 = this.fwdUnit.valueMuxB();
            this.exMemPipeline.MemDataWrite = operand2;            
            if(this.idExPipeline.ALUSrc)
                operand2 = this.idExPipeline.Offset;            
            //ALU manupulation codes are below
            if(operand1==operand2)
                this.exMemPipeline.ALU_zero = true;           
            this.exMemPipeline.ALU_result = this.ALUnit.process(operand1,
                    operand2,this.idExPipeline.function,this.idExPipeline.ALUOp);

            //following if else block selects the destination register for the next stage
            if(this.idExPipeline.RegDest)
                this.exMemPipeline.destReg = this.idExPipeline.rd;
            else
                this.exMemPipeline.destReg = this.idExPipeline.rt;
           
            this.exMemPipeline.iString = this.idExPipeline.iString;
            //Data Needed for visualization part
            this.tempExI32Offset = this.idExPipeline.i32Offset;
            this.tempExFunction = this.idExPipeline.function;
            this.tempExShiftLeft2Offset = this.tempExI32Offset*4;
            this.tempExPC = this.idExPipeline.PC;
            this.tempExAluOp = this.idExPipeline.ALUOp;
            this.tempExDestReg = this.idExPipeline.RegDest;
            this.tempExAluSource = this.idExPipeline.ALUSrc;
            this.tempExOperand2 = operand2;
            this.tempExRd = this.idExPipeline.rd;
        } else
            this.exMemPipeline = null;       
        
        if(this.ifIdPipeline!=null) {
            this.idExPipeline = new ID_EX_Pipeline();
            this.cUnit = new ControlUnit(this.ifIdPipeline.Instruction.op);
            
            this.idExPipeline.WB = this.cUnit.WB;
            this.idExPipeline.MEM = this.cUnit.MEM;
            this.idExPipeline.ALUOp = this.cUnit.ALUOp;
            this.idExPipeline.ALUSrc = this.cUnit.ALUSource;
            this.idExPipeline.RegDest = this.cUnit.RegDest;
            this.idExPipeline.PC = this.ifIdPipeline.PC;
            this.idExPipeline.rsValue = this.register[this.ifIdPipeline.Instruction.rs];
            this.idExPipeline.rtValue = this.register[this.ifIdPipeline.Instruction.rt];
            this.idExPipeline.Offset = this.ifIdPipeline.Instruction.offsetIJ;
            this.idExPipeline.i32Offset = this.idExPipeline.Offset;
            this.idExPipeline.rs = this.ifIdPipeline.Instruction.rs;
            this.idExPipeline.rt = this.ifIdPipeline.Instruction.rt;
            this.idExPipeline.function = this.ifIdPipeline.Instruction.function;
            this.idExPipeline.iString = this.ifIdPipeline.iString;
            this.idExPipeline.bitString = this.ifIdPipeline.Instruction.getBitString();
            
            if(this.ifIdPipeline.Instruction.rd>=0)                     
                this.idExPipeline.rd = this.ifIdPipeline.Instruction.rd;
            else
                this.idExPipeline.rd = Integer.parseInt(this.ifIdPipeline.Instruction.getBitString().substring(16,21),2);
          
            //Input For Hazard UNIT
            this.hdUnit.ifIdRs = this.ifIdPipeline.Instruction.rs;
            this.hdUnit.ifIdRt = this.ifIdPipeline.Instruction.rt;
            this.hdUnit.ifType = this.ifIdPipeline.Instruction.type;            
        } else
            this.idExPipeline = null;
        
        if(this.hdUnit.checkBranchHazard())
        {
            this.exMemPipeline.Flush();
            this.idExPipeline.Flush();
            this.ifIdPipeline = null;
            this.nextPC = this.hdUnit.branchAddress; // needed only for the visualization part
            return this.hdUnit.branchAddress;
        }

        if(this.hdUnit.checkDataHazard())
        {
            this.idExPipeline.Flush();
            // this.ifIdPipeline = null;

            this.nextPC = PC;// needed only for the visualization part
            return PC;
            // this.nextPC = PC-4;
            // return PC-4;
        }
        
        if(ins==null||ins.type=='U'||ins.type=='N')
            this.ifIdPipeline = null;         
        else
           this.ifIdPipeline = new IF_ID_Pipeline(ins,PC+4);
        
        this.nextPC = PC+4; // needed only for the visualization part
        return PC + 4;
    }

    public MFrame getCopy()
    {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return (MFrame)obj;
    }    
    void OUT(String s)
    {
        System.out.println(s);
    }

    int tempExAluOp;
    boolean tempExDestReg;
    boolean tempExAluSource;
    int tempExPC;
    int tempExI32Offset;
    int tempExFunction;
    int tempExShiftLeft2Offset;
    int tempExOperand2;
    int tempExRd;
    boolean tempMemMRead;
    boolean tempMemMWrite;
    int tempMemWriteData;
    int tempWbMData;
    int tempWbAluResult;
    boolean tempWbRegWrite;
    boolean tempWbMemToReg;
}