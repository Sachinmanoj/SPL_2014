/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package codeencounters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manoj Ramachandran
 */
public class Game {
    File folder;
    Code_Encounters CE;
    int icount;
    File input_file;
    File output_file;
    File info_file;
    File Game_over;
    File ready;
    String Board9x9[];
    String BoardCells[][][];
    int MiniBoard;
    int Self_PP3,Self_PP2;
    int Opp_PP3,Opp_PP2;
    int InfoErr;
    int prt,fill,cellChoose,cellFound;
    String self,opp;
    
    public Game(Code_Encounters c)
    {
        CE = c;  
        icount = 1;
        Board9x9 = new String[9];
        BoardCells = new String[9][3][3];
        InfoErr = 0;
        self="_";
        opp="_";
        prt = 0;
        fill = -1;
        cellFound = -1;
    }
    
    public void CreateReady() throws IOException
    {
        folder = new File(CE.Folder+"\\Code Encounters");
        //if(false)
        
        if(folder.exists())
        {
            String[] list = folder.list();
            if (list != null) 
            {
                for (int i = 0; i < list.length; i++) 
                {
                    File entry = new File(folder, list[i]);
                    entry.delete();
                }
            }
        }
        folder.mkdirs();
        ready = new File(folder+"\\ready.txt");
    }
    
    public void StartPlay() throws FileNotFoundException, IOException
    {
        Boolean x =true;
        while(x)
        {
            
            if(!ready.exists())
            {
                ready.createNewFile();
            }
            String input="\\Code Encounters_"+icount+"_i.txt";            
            
            input_file = new File(folder+input);           
            //System.out.println(folder.getPath()+input);
            
            if(input_file.exists())
            {                
                try 
                {
                    Thread.sleep(CE.td);
                } 
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                ReadInputFile();
                BoardCreation();
                CreateOutputFile();
                CheckInfoFile();
                icount++;
            }
            else if(new File(folder+"\\gameover.txt").exists())
            {
                x=false;
            }
            else
            {
                try 
                {
                    Thread.sleep(10);
                } 
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void ReadInputFile() throws FileNotFoundException, IOException
    {
        String This_Line;
        int RowCount=0;
        FileReader fr = new FileReader(input_file);
        BufferedReader line = new BufferedReader(fr);
        while((This_Line = line.readLine()) != null)
        {
            if(RowCount < 9)
            {
                this.Board9x9[RowCount] = This_Line;                        
            }
            else if(RowCount == 9)
            {
                this.MiniBoard = Integer.parseInt(This_Line);
            }
            else if(RowCount == 10)
            {
                String temp[] = This_Line.split(" ");
                this.Self_PP3 = Integer.parseInt(temp[0]);
                this.Self_PP2 = Integer.parseInt(temp[1]);
            }
            else if(RowCount == 11)
            {
                String temp[] = This_Line.split(" ");
                this.Opp_PP3 = Integer.parseInt(temp[0]);
                this.Opp_PP2 = Integer.parseInt(temp[1]);
            }

            //System.out.println(This_Line);

            RowCount++;
        }     
        fr.close();
                
    }
    
    public void BoardCreation()
    {
        int x=0,y=0,z=0;
        int u=-3;
        for(int i=0;i<9;i++)
        {
            if(i%3==0)
            {
                u+=3;
            }
            x=u;
            String values[]= this.Board9x9[i].split(" ");
            for(int j=0;j<9;j++)
            {
                this.BoardCells[x][y][z] = values[j];
                z=(z+1)%3;
                if(z==0)
                {
                    x++;
                }
            }
            y=(y+1)%3;
            
        }
        
        //printBoard();
        
        if(this.icount == 1)
        {
            if(this.MiniBoard == 0)
            {
                self = "X";
                opp = "O";
            }
            else
            {
                self = "O";
                opp = "X";
            }
        }
    }
    
    public void printBoard()
    {
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<3;j++)
            {
                for(int k=0;k<3;k++)
                {
                    System.out.print(this.BoardCells[i][j][k]);
                }
            }
            System.out.println();
        }
    }
    
    public void CreateOutputFile() throws IOException
    {
        String output = "\\Code Encounters_"+this.icount+"_o.txt";
        int cell_num = -1;
        output_file = new File(this.folder+output);           
        //System.out.println(folder.getPath()+output);
        
        output_file.createNewFile();
        
        //if(output_file.exists())
        //    System.out.print("exists");
        
        FileWriter fw = new FileWriter(output_file.getAbsoluteFile());
        PrintWriter pw = new PrintWriter(fw);
	
        if(this.MiniBoard != 0)
        {
            Boolean check = UsePP2(this.MiniBoard);
            if(check && this.Self_PP2 > 0)
            {
                cell_num = 0;
                pw.write(this.MiniBoard + " "+this.MiniBoard);
                this.BoardCells[this.MiniBoard - 1][(this.MiniBoard-1)/3][(this.MiniBoard-1)%3]=this.self;
                int cellpos = this.SelectCell(this.MiniBoard);
                if(cellpos != -1 && cellpos != 0)
                {
                    pw.println();                        
                    pw.write(this.MiniBoard + " "+cellpos);
                }
                //System.out.println(this.MiniBoard + " "+this.cellChoose);
            }
            else
            {
                int tempbrd,tempcell;
                cell_num =  SelectCell(this.MiniBoard);   
                if(cell_num != -1)
                {
                    //System.out.println(this.MiniBoard + " "+cell_num);
                    pw.write(this.MiniBoard + " "+cell_num);
                    this.BoardCells[this.MiniBoard - 1][(cell_num-1)/3][(cell_num-1)%3]=this.self;
                    if(this.Self_PP2 > 0 || this.Self_PP3 > 0)
                    {                
                        if(PowerPlayPossible(cell_num))
                        {
                            tempbrd = cell_num;
                            tempcell = this.cellChoose;
                            if(this.Self_PP2 > 0)
                            {
                                pw.println();                        
                                pw.write(cell_num + " "+this.cellChoose);
                            }
                            this.BoardCells[cell_num - 1][(this.cellChoose-1)/3][(this.cellChoose-1)%3]=this.self;
                            cell_num = this.cellChoose;
                            if(this.Self_PP3 > 0)
                            {
                                if(PowerPlayPossible(cell_num))
                                {
                                    if(this.Self_PP2 == 0)
                                    {
                                        pw.println();                        
                                        pw.write(tempbrd + " "+tempcell);
                                    }
                                    pw.println();
                                    pw.write(cell_num + " "+this.cellChoose);
                                    this.BoardCells[cell_num - 1][(this.cellChoose-1)/3][(this.cellChoose-1)%3]=this.self;
                                }
                             }
                        }
                    }
                }
            }
        }        
        if(cell_num == -1)
        {
            int max = this.NextBroardPriority(1);
            int brd = 1;
            for(int i=2;i<10;i++)
            {
                int x = this.NextBroardPriority(i);
                if(max < x)
                {
                    max = x;
                    brd = i;
                }
               
            }
            cell_num=SelectCell(brd);   
            pw.write(brd + " "+cell_num);
            
        }
        pw.close();
    }
    
    public boolean UsePP2(int Brd)
    {
        Brd--;
        int sp=0,empty=0;
        if(this.BoardCells[Brd][Brd/3][Brd%3].equals("_"))
        {
            //System.out.println(Brd/3+" "+Brd%3);
            int i=Brd/3;
            int j=Brd%3;
            if(i == j)
            {
                for(int k=0;k<3;k++)
                {
                    if(this.BoardCells[Brd][k][k].equals(this.self))
                    {
                        sp++;
                    }
                    else if(this.BoardCells[Brd][k][k].equals("_"))
                    {                        
                        empty++;
                    }
                }
                if(sp == 1 && empty == 2)
                {                    
                    return true;
                }
            }
            sp=0;
            empty=0;
            if((i+j) == 2)
            {
                for(int k=0,l=2;k<3;k++,l--)
                {
                    if(this.BoardCells[Brd][k][l].equals(this.self))
                    {
                        sp++;
                    }
                    else if(this.BoardCells[Brd][k][l].equals("_"))
                    {
                        empty++;
                    }
                }
                if(sp == 1 && empty == 2)
                {
                    return true;
                }
            }
            sp=0;
            empty=0;
            for(int k=0;k<3;k++)
            {
                if(this.BoardCells[Brd][i][k].equals(this.self))
                {
                    sp++;
                }
                else if(this.BoardCells[Brd][i][k].equals("_"))
                {
                    empty++;
                }
            }
            if(sp == 1 && empty == 2)
            {
                return true;
            }
            sp=0;
            empty=0;
            for(int k=0;k<3;k++)
            {
                if(this.BoardCells[Brd][k][j].equals(this.self))
                {
                    sp++;
                }
                else if(this.BoardCells[Brd][k][j].equals("_"))
                {
                    empty++;
                }
            }
            if(sp == 1 && empty == 2)
            {
                return true;
            }
        }
        return false;
    }
    
    public int SelectAnyMiniBoard()
    {
        for(int i=1;i<10;i++)
        {
            if(SelectCell(i) != -1)
                return i;
        }
        return -1;
    }
    
    public int SelectCell(int x)
    {
        x--;
        int priority[][]=new int[3][3];
        Boolean fillCheck = true;
        
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(this.BoardCells[x][i][j].equals("_"))
                {
                    fillCheck = false;
                    priority[i][j]=0;                                      
                    priority[i][j]+=diagonalStrike(x,i,j);
                    priority[i][j]+=RowColoumnStrike(x,i,j);
                    priority[i][j]+=BoardEmpty(((i*3) + j + 1));   
                    if(this.Self_PP2 == 0 && this.Self_PP3 == 0)
                    {
                        priority[i][j]+=OpponentViewOnTheBoard(((i*3) + j + 1));
                    }
                    if(x == 4 || ((i*3) + j + 1) == 5)
                    {
                        priority[i][j]+=1;
                    }                      
                    
                }
                else
                {
                    priority[i][j]=-1;
                }
            }
        }
        
        if(fillCheck)
        {
            return -1;
        }
        
        int max = -1;
        int cell = -1;
        
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(max<priority[i][j])
                {
                    max = priority[i][j];
                    cell = ((i*3) + j + 1);
                }
            }
        }
        
        return cell;
    }
    
    public int diagonalStrike(int x,int i,int j)
    {
        int sp=0,op=0;
        int count1=0, count2=0;
        if(i==j)
        {
            for(int k=0;k<3;k++)
            {
                if(this.BoardCells[x][k][k].equals(opp))
                {
                    op++;
                }
                else if(this.BoardCells[x][k][k].equals(self))
                {
                    sp++;
                }                    
            }
            if(sp == op)
            {
                count1 = 0;
            }
            else if((sp+op) == 1)
            {
                count1 = 1;
            }
            else
            {
                count1 = 5;
            }
        }
        if((i+j) == 2)
        {
            sp=op=0;
            for(int k=0,l=2;k<3;k++,l--)
            {
                if(this.BoardCells[x][k][l].equals(opp))
                {
                    op++;
                }
                else if(this.BoardCells[x][k][l].equals(self))
                {
                    sp++;
                }                    
            }
            if(sp == op)
            {
                count2 = 0;
            }
            else if((sp+op) == 1)
            {
                count2 = 1;
            }
            else
                count2 = 5;
        }
        return count1>count2?count1:count2;
    }
    
    // PLEASE IMOROVE
    public int RowColoumnStrike(int x,int i,int j)
    {
        int spr=0,opr=0,spc=0,opc=0;
        for(int k=-2;k<3;k++)
        {
            if(k==0)
            {
                k++;
            }
            try
            {
                if(this.BoardCells[x][i+k][j+0].equals(opp))
                {
                    opr++;
                }
                else if(this.BoardCells[x][i+k][j+0].equals(self))
                {
                    spr++;
                }    
            }
            catch(Exception e)
            {}
            try
            {
                if(this.BoardCells[x][i+0][j+k].equals(opp))
                {
                    opc++;
                }
                else if(this.BoardCells[x][i+0][j+k].equals(self))
                {
                    spc++;
                }    
            }
            catch(Exception e)
            {}
        }
        if(spr>1 || opr>1 || spc>1 || opc>1)
        {
            return 4;
        }
        if(spc == opc || spr ==opr)
        {
            return 0;
        }
        if(opc == 1 && opr == 1) 
        {
            return 0;
        }
        return 1;
    }
    
    public int BoardEmpty(int Brd)
    {
        Brd--;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(!this.BoardCells[Brd][i][j].equals("_"))
                {
                    return 0;
                }
            }
        }
        return 1;
    }
    
    // CAN IMPROVE
    public int OpponentViewOnTheBoard(int x)
    {
        // We should implement the Power Play 2
        
        prt = 0;
        fill = BoardFilled(x);
        if(fill == 0)
        {
            prt = NextBroardPriority(x);
        
            if(prt < 24)
                return 2;
            else if(prt < 48)
                return 1;
            else
                return 0;
        }
        return 0;
        
    }
    
    public int NextBroardPriority(int x)
    {
        x--;
        int priority[][]=new int[3][3];
        prt=0;
        
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(this.BoardCells[x][i][j].equals("_"))
                {
                    priority[i][j]=0;                                      
                    priority[i][j]+=diagonalStrike(x,i,j);
                    priority[i][j]+=RowColoumnStrike(x,i,j);
                    priority[i][j]+=BoardEmpty(((i*3) + j + 1));
                    if(x == 4 || ((i*3) + j + 1) == 5)
                    {
                        priority[i][j]++;
                    }  
                }
                else
                {
                    priority[i][j]=-1;
                }
                prt = prt + priority[i][j];
            }
        }
        
        return prt;
    }
    
    public boolean PowerPlayPossible(int Brd)
    {
        int choose = -1;
        int sp=0,empty = 0;
        Brd--;
        for(int k=0;k<3;k++)
        {
            if(this.BoardCells[Brd][k][k].equals(self))
            {
                sp++;
            }             
            else if(this.BoardCells[Brd][k][k].equals("_"))
            {
                choose = (k*3) + k + 1;
                empty = 1;
            }
        }
        if( sp == 2 && empty == 1)
        {
            this.cellChoose = choose;
            return true;
        }
        sp=0;
        empty = 0;
        for(int k=0,l=2;k<3;k++,l--)
        {
            if(this.BoardCells[Brd][k][l].equals(self))
            {
                sp++;
            }         
            else if(this.BoardCells[Brd][k][l].equals("_"))
            {
                choose = (k*3) + l + 1;
                empty = 1;
            }
        }
        if( sp == 2 && empty == 1)
        {
            this.cellChoose = choose;
            return true;
        }
        sp=0;
        empty = 0;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(this.BoardCells[Brd][i][j].equals(self))
                {
                    sp++;
                }
                if(this.BoardCells[Brd][i][j].equals("_"))
                {
                    choose = (i*3) + j + 1;
                    empty = 1;
                }
            }
            if(sp == 2 && empty == 1)
            {
                this.cellChoose = choose;
                return true;
            }
        }
        sp=0;
        empty = 0;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(this.BoardCells[Brd][j][i].equals(self))
                {
                    sp++;
                }
                if(this.BoardCells[Brd][j][i].equals("_"))
                {
                    choose = (j*3) + i + 1;
                    empty = 1;
                }
            }
            if(sp == 2 && empty == 1)
            {
                this.cellChoose = choose;
                return true;
            }
        }
        return false;
        
    }  
    
    public int BoardFilled(int Brd)
    {
        Brd--;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(this.BoardCells[Brd][i][j].equals("_"))
                {
                    return 0;
                }
            }
        }
        return 1;
    }
    
    public void CheckInfoFile() throws FileNotFoundException, IOException
    {
        String info="\\Code Encounters_"+icount+"_info.txt";
        info_file = new File(folder+info);           
        //System.out.println(folder.getPath()+info);
        while(true)
        {
            try 
            {
                Thread.sleep(10);
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(info_file.exists())
            {                
                try 
                {
                    Thread.sleep(CE.td);
                } 
                catch (InterruptedException ex) 
                {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }

                FileReader fr = new FileReader(info_file);
                BufferedReader line = new BufferedReader(fr);
                this.InfoErr = Integer.parseInt(line.readLine());
                if(this.InfoErr != 0)
                {
                    System.out.println(this.InfoErr);
                }
                fr.close();  
                break;
            }
        }
    }
    
}
