fid=fopen('del.txt', 'wt'); 
for i=1:10000
   fprintf(fid, '%s', strcat('t#',num2str(i),',u#', num2str(Udel_g(i,1))));
   for j=1:25
      fprintf(fid, '%s', strcat(',a#',num2str((i-1)*25+j),'>'));
      for k=1:25
          fprintf(fid, '%s', strcat(num2str(k),':',num2str(Xdel_g(i,j,k))));
          if(k<25)
              fprintf(fid, '%s', ' ');
          else
             fprintf(fid, '%s', strcat('>',num2str(Ydel_g(i,j)))); 
          end
      end
   end
   fprintf(fid, '\n');
end
fclose(fid);