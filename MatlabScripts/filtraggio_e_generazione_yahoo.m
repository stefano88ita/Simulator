%launch the_good_filter_beta and get U2 and good_rounds
res=1;
res2=1;
clear res;
clear res2;
clear Afiltered;
%find users that
%for i=1:1861 %number_of_user
%    if(sum(Udel_g(:)==i)>8) %limit
%        res(i)=1;
%        for j=1:10000
%           if(Udel_g(j)==i)
%               res2(j)=1;
%           end
%        end
%    end
%end

fid=fopen('filtered_yahoo.txt', 'wt'); 
j=1;
for i=1:size(good_rounds,1)
    fprintf(fid, '%s', strcat('t#',num2str(i),',u#', num2str(U2(good_rounds(i)))));
    for j=1:size(X(1,1,:),3)
        if(X(good_rounds(i),1,j)>0)
            reward='none';
            if(R(good_rounds(i))==j)
                reward=num2str(Y(good_rounds(i)));
            end
            fprintf(fid, '%s', strcat(',a#',num2str(X(good_rounds(i),1,j)),'>',num2str(X(good_rounds(i),1,j)),'>1:',reward));
        end
       
    end
    fprintf(fid, '\n');
end
fclose(fid);

%m=1;
%clear res3;
%res3=zeros(size(res,2));
%for(i=1:size(res,2))
%   if(res(i)==1)
%       n=1;
%      for(j=1:size(res,2))
%          if(res(j)==1)
%              if(i~=j)
%                res3(m,n)=Adel(i,j);
%              else
%                  res3(m,n)=0;
%              end
%          n=n+1;    
%          end
%          end
%      res3(m,m)=sum(res3(m,:))*-1;
%      m=m+1;
%   end
%end

%csvwrite('delicious_filtrato.csv',res3);

