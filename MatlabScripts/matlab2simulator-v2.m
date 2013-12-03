fid=fopen('dataset_finto.txt', 'wt'); 
for i=1:100
   fprintf(fid, '%s', strcat('t#',num2str(i),',u#1'));
   for j=1:10
      fprintf(fid, '%s', strcat(',a#',num2str((i-1)*10+j),'>'));
      for k=1:20
          fprintf(fid, '%s', strcat(num2str(k),':',num2str(X(i,k,j))));
          if(k<20)
              fprintf(fid, '%s', ' ');
          else
              if(j>1)
                fprintf(fid, '%s', strcat('>',num2str(0)));   
              else
                 fprintf(fid, '%s', strcat('>',num2str(1)));  
              end
             %fprintf(fid, '%s', strcat('>',num2str(Y(i,j)))); 
          end
      end
   end
   fprintf(fid, '\n');
end
fclose(fid);