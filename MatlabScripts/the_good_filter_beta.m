function [U2, good_rounds] = the_good_filter_beta(U, threshold)
% Select from U the set of users occurring at least threshold times
% then renames the surviving users from 1 to ... the resulting no of such
% users
% good_rounds is the set of rounds where such surviving users occur.
% Then invoke the function on X, Y, U and R like the following
% 
% CLUBBandit_no_features_v8(X(good_rounds,1,:),Y(good_rounds),U2(good_rounds),R(good_rounds),alpha,alpha2)
%
% Reviewed by Shuai
% Improved by CG
% Modified by CG on Jan 14th, 2014: added an upper and lower threshold
% Rollback by Shuai Jan 16th, 2014

m = max(U);
n = zeros(m,1);
for i = 1 : numel(U) 
    n(U(i)) = n(U(i))+1; 
end

%Attempted to access w(145); index out of bounds because numel(w)=144.
%Error in LinUCB_One_no_features_v2 (line 77)
%            p(k) = w(tmpX) + alpha * sqrt( log(t+1) / M(tmpX) );
%good_usersl = find( n > lthreshold ) ;
%good_usersu = find( n < uthreshold ) ;
%good_users = intersect (good_usersl, good_usersu);
good_users = find(n >= threshold);

user_filter = ismember(U, good_users); 
good_rounds = find(user_filter == 1);

U2 = zeros(numel(U), 1);

for i = 1 : numel(good_rounds) 
    U2(good_rounds(i)) = find(good_users == U(good_rounds(i)),1); 
end

end
