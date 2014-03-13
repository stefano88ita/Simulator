fid=fopen('two_cliques.txt', 'wt'); 
for i=1:10000
   fprintf(fid, '%s', strcat('t#',num2str(i),',u#', num2str(U(i,1))));
   for j=1:5
      fprintf(fid, '%s', strcat(',a#',num2str((i-1)*5+j),'>'));
      for k=1:20
          fprintf(fid, '%s', strcat(num2str(k),':',num2str(X(i,k,j))));
          if(k<20)
              fprintf(fid, '%s', ' ');
          else
             fprintf(fid, '%s', strcat('>',num2str(Y(i,j)))); 
          end
      end
   end
   fprintf(fid, '\n');
end
fclose(fid);