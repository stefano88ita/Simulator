%please load the .mat file
res=1;
res2=1;
clear res;
clear res2;
clear Afiltered;
%find users that
for i=1:1861 %number_of_user
    if(sum(Udel_g(:)==i)>8) %limit
        res(i)=1;
        for j=1:10000
           if(Udel_g(j)==i)
               res2(j)=1;
           end
        end
    end
end

fid=fopen('delicious_filtrato.txt', 'wt'); 
j=1;
for i=1:size(res2,2)
   if(res2(i)==1)
        sum(res(1:Udel_g(i,1)))+1
       fprintf(fid, '%s', strcat('t#',num2str(j),',u#', num2str(  sum(res(1:Udel_g(i,1)))+1 )));
       for j=1:25
          fprintf(fid, '%s', strcat(',a#',num2str((i-1)*25+j),'>'));
          for k=1:25
              fprintf(fid, '%s', strcat(num2str(k),':',num2str(Xdel_g(i,k,j))));%invertito
              if(k<25)
                  fprintf(fid, '%s', ' ');
              else
                 fprintf(fid, '%s', strcat('>',num2str(Ydel_g(i,j)))); 
              end
          end
       end
       fprintf(fid, '\n');
       j=j+1;
   end
end
fclose(fid);

m=1;
clear res3;
res3=zeros(size(res,2));
for(i=1:size(res,2))
   if(res(i)==1)
       n=1;
      for(j=1:size(res,2))
          if(res(j)==1)
              if(i~=j)
                res3(m,n)=Adel(i,j);
              else
                  res3(m,n)=0;
              end
          n=n+1;    
          end
          end
      res3(m,m)=sum(res3(m,:))*-1;
      m=m+1;
   end
end

csvwrite('delicious_filtrato.csv',res3);

